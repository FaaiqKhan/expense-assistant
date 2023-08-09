package com.practice.expenseAssistant.ui.common

import android.app.DatePickerDialog
import android.content.res.Configuration
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import java.time.LocalDate
import java.util.Calendar

@Composable
fun DatePicker(modifier: Modifier = Modifier, onSelect: (date: LocalDate) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Fetching current year, month and day
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

    var selectedDate by remember { mutableStateOf("$dayOfMonth/${month + 1}/$year") }

    val datePicker =
        DatePickerDialog(context, { _: DatePicker,
                                    selectedYear: Int,
                                    selectedMonth: Int,
                                    selectedDayOfMonth: Int ->
            selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
            onSelect(LocalDate.of(selectedYear, (selectedMonth + 1), selectedDayOfMonth))
        }, year, month, dayOfMonth)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Date: $selectedDate",
            modifier = Modifier.clickable { datePicker.show() },
            style = MaterialTheme.typography.displayMedium
        )
        Text(text = "day/month/year", style = MaterialTheme.typography.labelMedium)
        TextButton(
            onClick = {
                selectedDate = "$dayOfMonth/${month + 1}/$year"
                onSelect(LocalDate.of(year, (month + 1), dayOfMonth))
            }
        ) { Text(text = "Today") }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDatePicker() {
    ExpenseAssistantTheme {
        DatePicker(modifier = Modifier.fillMaxWidth(), onSelect = {})
    }
}