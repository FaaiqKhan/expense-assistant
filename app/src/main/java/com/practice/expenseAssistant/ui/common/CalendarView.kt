package com.practice.expenseAssistant.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import com.practice.expenseAssistant.utils.Utils
import java.time.LocalDate

@Composable
fun CalendarView(modifier: Modifier = Modifier, localDate: LocalDate) {

    val combinedDates = Utils.createCalenderDays(localDate)

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(Utils.daysMap.size),
            horizontalArrangement = Arrangement.Center
        ) {
            items(Utils.daysMap.keys.toList()) { items -> Text(text = items.substring(0, 3)) }
            items(combinedDates) { items -> Text(text = items.toString()) }
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