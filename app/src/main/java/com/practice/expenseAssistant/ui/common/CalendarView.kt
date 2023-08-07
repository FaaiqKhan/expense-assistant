package com.practice.expenseAssistant.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.ui.homeScreen.ExpenseAssistantViewModel
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import com.practice.expenseAssistant.utils.Utils

@Composable
fun CalendarView(
    modifier: Modifier = Modifier,
    expenseAssistant: ExpenseAssistantViewModel,
) {
    val calendarDatesState by expenseAssistant.calenderDates.collectAsState()

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.screen_content_padding)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "${expenseAssistant.calendarOfMonth.month} ${expenseAssistant.calendarOfMonth.year}",
                style = MaterialTheme.typography.displaySmall
            )
            Text(
                text = "01 - ${expenseAssistant.calendarOfMonth.month.maxLength()} ${expenseAssistant.calendarOfMonth.month}",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = stringResource(R.string.today),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.clickable { expenseAssistant.backToToday() }
            )
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
            items(calendarDatesState) { item ->
                CalendarCard(
                    indexInList = item.id,
                    calendarDateState = item,
                    onSelect = expenseAssistant::updateSelectedDate
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCalendarView() {
    ExpenseAssistantTheme {
        CalendarView(expenseAssistant = viewModel())
    }
}