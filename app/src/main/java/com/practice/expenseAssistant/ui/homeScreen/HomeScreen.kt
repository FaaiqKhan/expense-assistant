package com.practice.expenseAssistant.ui.homeScreen

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.data.*
import com.practice.expenseAssistant.ui.common.*
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import com.practice.expenseAssistant.utils.*
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeScreenViewModel = hiltViewModel(),
    onTransactionSelect: (transaction: TransactionModel) -> Unit
) {
    val calendar by homeViewModel.getCalender().collectAsState()
    val localCalendarState by homeViewModel.localCalender.collectAsState()
    val transactions = homeViewModel.getTransactionsBySelectedDate()

    HomeScreenContent(
        modifier = modifier,
        calendar = calendar,
        transactions = transactions,
        userName = homeViewModel.getUser().name,
        calendarUiState = localCalendarState,
        onToday = homeViewModel::backToToday,
        onDateUpdate = homeViewModel::updateSelectedDate,
        onSelect = onTransactionSelect
    )
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier,
    userName: String,
    calendarUiState: HomeScreenUiState,
    calendar: List<CalendarDateModel>,
    transactions: List<TransactionModel>,
    onToday: () -> Unit,
    onDateUpdate: (index: Int) -> Unit,
    onSelect: (transaction: TransactionModel) -> Unit
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
            date = LocalDate.now(),
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
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.calendar_padding)))
        LazyColumn(
            modifier = Modifier.padding(
                horizontal = dimensionResource(id = R.dimen.border_stroke)
            ),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(id = R.dimen.element_spacing)
            )
        ) {
            items(count = transactions.size, key = { transactions[it].time.nano }) {
                BriefTransactionCard(
                    transaction = transactions[it],
                    onClick = { onSelect(transactions[it]) },
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewHomeScreen() {
    val dates =
        Utils.createCalenderDays(LocalDate.of(2023, LocalDate.now().month, 1), LocalDate.now())
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
            calendar = dates,
            transactions = listOf(
                TransactionModel(
                    categoryType = CategoryType.EXPENSE,
                    category = ExpenseType.BILL,
                    note = "Electricity bill with Gas bill and Water bill",
                    amount = 100.00,
                    date = LocalDate.now(),
                    time = LocalTime.now(),
                ),
                TransactionModel(
                    categoryType = CategoryType.EXPENSE,
                    category = ExpenseType.BILL,
                    note = "Electricity bill with ",
                    amount = 100.00,
                    date = LocalDate.now(),
                    time = LocalTime.now(),
                )
            ),
            onSelect = {}
        )
    }
}