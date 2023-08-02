package com.practice.expenseAssistant.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.ui.homeScreen.HomeScreenViewModel
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import com.practice.expenseAssistant.utils.Utils

@Composable
fun CalendarView(
    modifier: Modifier = Modifier,
    homeViewModel: HomeScreenViewModel,
) {
    val calendarDatesState by homeViewModel.calenderDates.collectAsState()

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "${homeViewModel.calendarOfMonth.month} ${homeViewModel.calendarOfMonth.year}",
                style = MaterialTheme.typography.displaySmall
            )
            Text(
                text = "01 - ${homeViewModel.calendarOfMonth.month.maxLength()} ${homeViewModel.calendarOfMonth.month}",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.element_spacing)))
            CalendarIcon(date = homeViewModel.selectedDate)
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.element_spacing)))
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
                    content = "",
                    onSelect = homeViewModel::updateSelectedDate
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCalendarView() {
    ExpenseAssistantTheme {
        CalendarView(homeViewModel = viewModel())
    }
}