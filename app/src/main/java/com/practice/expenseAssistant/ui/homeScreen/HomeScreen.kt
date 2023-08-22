package com.practice.expenseAssistant.ui.homeScreen

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.data.*
import com.practice.expenseAssistant.ui.common.*
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import com.practice.expenseAssistant.utils.Utils
import java.time.LocalDate

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    expenseAssistant: HomeScreenViewModel
) {
    val name = expenseAssistant.getUser().name
    val localCalendarState by expenseAssistant.localCalender.collectAsState()
    val calendar = expenseAssistant.expenseAssistantRepository.getCalender().collectAsState()

    HomeScreenContent(
        modifier = modifier,
        userName = name,
        calendarUiState = localCalendarState,
        date = expenseAssistant.expenseAssistantRepository.getTodayDate(),
        calendar = calendar.value,
        onToday = expenseAssistant::backToToday,
        onDateUpdate = expenseAssistant::updateSelectedDate
    )
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier,
    userName: String,
    calendarUiState: HomeScreenUiState,
    date: LocalDate,
    calendar: List<CalendarDateModel>,
    onToday: () -> Unit,
    onDateUpdate: (index: Int) -> Unit,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.End) {
        Text(
            text = "Hi $userName",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.screen_content_padding))
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.element_spacing)))
        TotalExpenseCard(
            modifier = Modifier.fillMaxWidth(),
            uiState = calendarUiState,
            onClickViewAll = { }
        )
        CalendarView(
            date = date,
            calendar = calendar,
            backToToday = onToday,
            updateDate = onDateUpdate,
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.calendar_padding)))
        if (calendarUiState is HomeScreenUiState.Success) {
            OpenCloseBalanceCard(
                modifier = Modifier.fillMaxWidth(),
                openBalanceOfMonth = calendarUiState.balanceModel.openingBalance,
                closeBalanceOfMonth = calendarUiState.balanceModel.closingBalance,
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewHomeScreen() {
    val dates = Utils.createCalenderDays(LocalDate.now(), LocalDate.now())
    ExpenseAssistantTheme {
        HomeScreenContent(
            modifier = Modifier.fillMaxSize(),
            userName = "Faiq Ali Khan",
            calendarUiState = HomeScreenUiState.Success(
                CalendarDataModel(
                    localDate = LocalDate.now(),
                    localCalendar = dates,
                ),
                balanceModel = BalanceModel(
                    totalExpense = 400.00,
                    openingBalance = 1000.00,
                    closingBalance = 600.00
                )
            ),
            onToday = {},
            onDateUpdate = {},
            date = LocalDate.now(),
            calendar = dates
        )
    }
}