package com.practice.expenseAssistant.ui.homeScreen

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.data.*
import com.practice.expenseAssistant.ui.common.*
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import com.practice.expenseAssistant.ui.theme.spacing
import com.practice.expenseAssistant.utils.*
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeScreenViewModel = hiltViewModel(),
    onTransactionSelect: (transaction: TransactionModel) -> Unit,
    controller: NavHostController
) {

    val uiState by homeViewModel.uiState.collectAsState()
    val monthCashFlow by homeViewModel.getMonthCashFlow().collectAsState()

    Column(modifier = modifier) {
        ScreenHeader(
            controller = controller,
            userName = homeViewModel.getUserName(),
        )
        when (uiState) {
            is HomeScreenUiState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
                content = { CircularProgressIndicator() }
            )

            is HomeScreenUiState.Failure -> Text(text = "Hello there!")
            is HomeScreenUiState.Success -> {
                val calender by homeViewModel.getCalender().collectAsState()
                HomeScreenContent(
                    calendar = calender,
                    transactions = homeViewModel.getTransactionsBySelectedDate(),
                    cashFlow = monthCashFlow,
                    onDateUpdate = homeViewModel::updateSelectedDate,
                    onSelect = onTransactionSelect,
                    currentMonth = homeViewModel.getSelectedDate(),
                    viewMonth = { previous ->
                        val date = if (previous) {
                            homeViewModel.getSelectedDate().minusMonths(1)
                        } else {
                            homeViewModel.getSelectedDate().plusMonths(1)
                        }
                        homeViewModel.updateCalenderWithMonthYear(date)
                    }
                )
            }
        }
    }
}

@Composable
private fun HomeScreenContent(
    calendar: List<CalendarDateModel>,
    transactions: List<TransactionModel>,
    cashFlow: MonthCashFlow,
    onDateUpdate: (index: Int) -> Unit,
    onSelect: (transaction: TransactionModel) -> Unit,
    currentMonth: LocalDate,
    viewMonth: (previous: Boolean) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.End) {
        TotalExpenseCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = MaterialTheme.spacing.small),
            totalExpense = cashFlow.expense,
            onClick = {},
        )
        CalendarView(
            calendar = calendar,
            updateDate = onDateUpdate,
            currentDate = currentMonth,
            viewMonth = viewMonth,
            moveToToday = {}
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.calendar_padding)))
        OpenCloseBalanceCard(
            modifier = Modifier.fillMaxWidth(),
            openBalanceOfMonth = cashFlow.openingAmount,
            closeBalanceOfMonth = cashFlow.closingAmount,
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.calendar_padding)))
        LazyColumn(
            modifier = Modifier.padding(
                horizontal = dimensionResource(id = R.dimen.border_stroke)
            ),
            verticalArrangement = Arrangement.spacedBy(
                space = dimensionResource(id = R.dimen.element_spacing)
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
@Composable
private fun PreviewHomeScreen() {
    val date = LocalDate.now()
    ExpenseAssistantTheme {
        Surface(
            modifier = Modifier.padding(MaterialTheme.spacing.large)
        ) {
            Column {
                ScreenHeader(userName = "Faiq Ali Khan", controller = rememberNavController())
                HomeScreenContent(
                    cashFlow = MonthCashFlow(
                        income = 3000.0,
                        expense = 1000.0,
                        openingAmount = 3000.0,
                        closingAmount = 2000.0
                    ),
                    onDateUpdate = {},
                    calendar = Utils.createCalenderDays(
                        year = date.year,
                        month = date.monthValue,
                        date = date.dayOfMonth,
                    ),
                    transactions = listOf(
                        TransactionModel(
                            categoryType = CategoryType.EXPENSE,
                            category = ExpenseType.BILL,
                            note = "Electricity bill with Gas bill and Water bill",
                            amount = 100.00,
                            date = LocalDate.now(),
                            time = LocalTime.now(),
                            month = LocalDate.now().monthValue,
                            year = LocalDate.now().year
                        ),
                        TransactionModel(
                            categoryType = CategoryType.EXPENSE,
                            category = ExpenseType.BILL,
                            note = "Electricity bill with ",
                            amount = 100.00,
                            date = LocalDate.now(),
                            time = LocalTime.now(),
                            month = LocalDate.now().monthValue,
                            year = LocalDate.now().year
                        )
                    ),
                    onSelect = {},
                    currentMonth = LocalDate.now(),
                    viewMonth = {}
                )
            }
        }
    }
}