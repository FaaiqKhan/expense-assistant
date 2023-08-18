package com.practice.expenseAssistant.ui.common

import android.content.res.Configuration
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
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.ui.homeScreen.HomeScreenUiState
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import com.practice.expenseAssistant.utils.Utils

@Composable
fun CalendarView(
    modifier: Modifier = Modifier,
    uiState: HomeScreenUiState,
    backToToday: () -> Unit,
    updateDate: (index: Int) -> Unit,
) {
    when (uiState) {
        is HomeScreenUiState.Loading -> {}
        is HomeScreenUiState.Failure -> {}
        is HomeScreenUiState.Success -> {
            Column(modifier = modifier) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(id = R.dimen.screen_content_padding)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "${uiState.calendarData.localDate.month} ${uiState.calendarData.localDate.year}",
                        style = MaterialTheme.typography.displaySmall
                    )
                    Text(
                        text = "01 - ${uiState.calendarData.localDate.month.maxLength()} ${uiState.calendarData.localDate.month}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = stringResource(R.string.today),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.clickable { backToToday() }
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
                    items(uiState.calendarData.localCalendar) { item ->
                        CalendarCard(
                            indexInList = item.id,
                            calendarDateState = item,
                            onSelect = updateDate
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun PreviewCalendarView() {
    ExpenseAssistantTheme {
        CalendarView(uiState = HomeScreenUiState.Loading, backToToday = {}, updateDate = {})
    }
}