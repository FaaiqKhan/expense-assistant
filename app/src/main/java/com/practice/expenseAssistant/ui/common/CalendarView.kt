package com.practice.expenseAssistant.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.data.ExpenseModel
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import com.practice.expenseAssistant.utils.Utils
import java.time.LocalDate

@Composable
fun CalendarView(modifier: Modifier = Modifier, localDate: LocalDate) {

    val combinedDates: List<ExpenseModel> = Utils.createCalenderDays(localDate)

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.element_spacing)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "${localDate.month} ${localDate.year}",
                style = MaterialTheme.typography.displaySmall
            )
            Text(
                text = "01 - ${localDate.month.maxLength()} ${localDate.month}",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.element_spacing)))
            CalendarIcon(date = localDate)
        }
        Divider(color = Color.Gray)
        LazyVerticalGrid(
            columns = GridCells.Fixed(Utils.daysMap.size),
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(Utils.daysMap.keys.toList()) { items ->
                Text(
                    text = items.substring(0, 3),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            items(combinedDates) { items ->
                CalendarCard(
                    date = items.date,
                    content = items.expense,
                    isSelected = items.isSelected,
                    isCurrentMonthDate = items.isCurrentMonthDate
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCalendarView() {
    ExpenseAssistantTheme {
        CalendarView(localDate = LocalDate.now())
    }
}