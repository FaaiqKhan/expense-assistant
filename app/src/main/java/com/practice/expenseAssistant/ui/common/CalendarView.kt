package com.practice.expenseAssistant.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.data.CalendarDateModel
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import com.practice.expenseAssistant.utils.Utils
import java.time.LocalDate

@Composable
fun CalendarView(
    calendar: List<CalendarDateModel>,
    updateDate: (index: Int) -> Unit,
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        columns = GridCells.Fixed(Utils.daysMap.size),
    ) {
        items(Utils.daysMap.keys.toList()) { items ->
            Text(
                text = items.substring(0, 3),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        items(calendar) { item ->
            CalendarCard(
                onSelect = updateDate,
                indexInList = item.id,
                calendarDateState = item,
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun PreviewCalendarView() {
    val date = LocalDate.now().minusMonths(1)
    ExpenseAssistantTheme {
        CalendarView(
            calendar = Utils.createCalenderDays(
                year = date.year,
                month = date.monthValue,
                date = date.dayOfMonth,
            ),
            updateDate = {},
        )
    }
}