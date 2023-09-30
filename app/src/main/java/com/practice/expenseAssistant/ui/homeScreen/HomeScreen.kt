package com.practice.expenseAssistant.ui.homeScreen

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
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
    onTransactionSelect: (transaction: TransactionModel) -> Unit,
) {

    val uiState by homeViewModel.uiState.collectAsState()
    val monthCashFlow by homeViewModel.getMonthCashFlow().collectAsState()

    when (uiState) {
        is HomeScreenUiState.Loading -> CircularProgressIndicator()
        is HomeScreenUiState.Failure -> Text(text = "Hello there!")
        is HomeScreenUiState.Success -> {
            HomeScreenContent(
                modifier = modifier,
                userName = homeViewModel.getUser().name,
                calendar = (uiState as HomeScreenUiState.Success).calendar,
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

@Composable
private fun HomeScreenContent(
    modifier: Modifier,
    userName: String,
    calendar: List<CalendarDateModel>,
    transactions: List<TransactionModel>,
    cashFlow: MonthCashFlow,
    onDateUpdate: (index: Int) -> Unit,
    onSelect: (transaction: TransactionModel) -> Unit,
    currentMonth: LocalDate,
    viewMonth: (previous: Boolean) -> Unit
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
        TotalExpenseCard(modifier = Modifier.fillMaxWidth(), totalExpense = cashFlow.expense)
        CalenderNavigator(currentDate = currentMonth, viewMonth = viewMonth)
        Divider()
        CalendarView(calendar = calendar, updateDate = onDateUpdate)
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

@Composable
fun CalenderNavigator(currentDate: LocalDate, viewMonth: (previous: Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        TextButton(onClick = { viewMonth(true) }) {
            Icon(
                imageVector = Icons.Default.ArrowBackIos,
                contentDescription = stringResource(id = R.string.previous_month),
            )
            Text(
                text = Utils.decapitalizeStringExpectFirstLetter(
                    currentDate.minusMonths(1).month.name.substring(
                        0,
                        3
                    )
                ),
                style = MaterialTheme.typography.headlineLarge,
            )
        }
        Text(
            text = "${Utils.decapitalizeStringExpectFirstLetter(currentDate.month.name)} ${currentDate.year}",
            style = MaterialTheme.typography.headlineMedium
        )
        TextButton(onClick = { viewMonth(false) }) {
            Text(
                text = Utils.decapitalizeStringExpectFirstLetter(
                    currentDate.plusMonths(1).month.name.substring(
                        0,
                        3
                    )
                ),
                style = MaterialTheme.typography.headlineLarge,
            )
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = stringResource(id = R.string.next_month),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun PreviewHomeScreen() {
    val date = LocalDate.now()
    ExpenseAssistantTheme {
        HomeScreenContent(
            modifier = Modifier.fillMaxSize(),
            userName = "Faiq Ali Khan",
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