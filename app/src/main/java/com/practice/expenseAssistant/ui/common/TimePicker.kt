package com.practice.expenseAssistant.ui.common

import android.app.TimePickerDialog
import android.content.res.Configuration
import android.widget.TimePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import com.practice.expenseAssistant.utils.Utils
import java.time.LocalTime
import java.util.Calendar

@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    in24Hours: Boolean = false,
    onSelect: (time: LocalTime) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Fetching current hour, and minute
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    var selectedTime by remember { mutableStateOf(Utils.updateTo2Digits(hour, minute)) }

    val timePicker = TimePickerDialog(
        context,
        { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            selectedTime = Utils.updateTo2Digits(selectedHour, selectedMinute)
            onSelect(LocalTime.of(selectedHour, selectedMinute))
        }, hour, minute, in24Hours
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Time: $selectedTime",
            modifier = Modifier.clickable { timePicker.show() },
            style = MaterialTheme.typography.displayMedium
        )
        TextButton(
            onClick = {
                selectedTime = "${calendar[Calendar.HOUR_OF_DAY]}:${calendar[Calendar.MINUTE]}"
                onSelect(LocalTime.now())
            }
        ) { Text(text = "Current") }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewTimePicker() {
    ExpenseAssistantTheme {
        TimePicker(modifier = Modifier.fillMaxWidth(), onSelect = {})
    }
}