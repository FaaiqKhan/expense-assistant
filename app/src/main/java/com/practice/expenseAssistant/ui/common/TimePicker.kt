package com.practice.expenseAssistant.ui.common

import android.app.TimePickerDialog
import android.content.res.Configuration
import android.widget.TimePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import java.util.Calendar

@Composable
fun TimePicker(modifier: Modifier = Modifier, in24Hours: Boolean = false, onSelect: (time: String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Fetching current hour, and minute
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    var selectedTime by remember { mutableStateOf("$hour:$minute") }

    val timePicker = TimePickerDialog(
        context,
        { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            selectedTime = "$selectedHour:$selectedMinute"
            onSelect(selectedTime)
        }, hour, minute, in24Hours
    )

    Text(text = "Time: $selectedTime", modifier = modifier.clickable { timePicker.show() })
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewTimePicker() {
    ExpenseAssistantTheme {
        TimePicker(modifier = Modifier.fillMaxWidth(), onSelect = {})
    }
}