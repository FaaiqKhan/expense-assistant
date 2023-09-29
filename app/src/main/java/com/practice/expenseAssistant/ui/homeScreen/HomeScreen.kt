package com.practice.expenseAssistant.ui.homeScreen

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.*
import androidx.compose.material3.*
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeScreenViewModel = hiltViewModel(),
    onTransactionSelect: (transaction: TransactionModel) -> Unit,
) {
    var prevPage by remember { mutableStateOf(2) }

    val uiState by homeViewModel.uiState.collectAsState()
    val monthCashFlow by homeViewModel.getMonthCashFlow().collectAsState()

    val pagerState = rememberPagerState(pageCount = { 3 }, initialPage = 3)

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page: Int ->
            if (page < prevPage) {
                homeViewModel.updateCalenderOfMonthYear(
                    homeViewModel.getCurrentMonth().minusMonths(1)
                )
            } else if (page > prevPage) {
                homeViewModel.updateCalenderOfMonthYear(
                    homeViewModel.getCurrentMonth().plusMonths(1)
                )
            }
            prevPage = page

        }
    }

    when (uiState) {
        is HomeScreenUiState.Loading -> CircularProgressIndicator()
        is HomeScreenUiState.Failure -> Text(text = "Fuck it's")
        is HomeScreenUiState.Success -> {
            HomeScreenContent(
                modifier = modifier,
                calendar = (uiState as HomeScreenUiState.Success).calendar,
                transactions = homeViewModel.getTransactionsBySelectedDate(),
                userName = homeViewModel.getUser().name,
                cashFlow = monthCashFlow,
                onToday = homeViewModel::backToToday,
                onDateUpdate = homeViewModel::updateSelectedDate,
                onSelect = onTransactionSelect,
                pagerState = pagerState
            )
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun HomeScreenContent(
    modifier: Modifier,
    userName: String,
    calendar: List<CalendarDateModel>,
    transactions: List<TransactionModel>,
    cashFlow: MonthCashFlow,
    onToday: () -> Unit,
    onDateUpdate: (index: Int) -> Unit,
    onSelect: (transaction: TransactionModel) -> Unit,
    pagerState: PagerState
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
            totalExpense = cashFlow.expense,
        )
        HorizontalPager(
            state = pagerState,
            pageSpacing = dimensionResource(id = R.dimen.screen_content_padding)
        ) {
            HomeScreenPagerViewContent(
                calendar,
                transactions,
                cashFlow,
                onToday,
                onDateUpdate,
                onSelect,
            )
        }

    }
}

@Composable
fun HomeScreenPagerViewContent(
    calendar: List<CalendarDateModel>,
    transactions: List<TransactionModel>,
    cashFlow: MonthCashFlow,
    onToday: () -> Unit,
    onDateUpdate: (index: Int) -> Unit,
    onSelect: (transaction: TransactionModel) -> Unit,
) {
    Column {
        CalendarView(
            date = LocalDate.now(),
            calendar = calendar,
            backToToday = onToday,
            updateDate = onDateUpdate,
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

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun PreviewHomeScreen() {
    val dates = Utils.createCalenderDays(
        LocalDate.of(2023, LocalDate.now().month, 1),
        LocalDate.now(),
    )
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
            pagerState = rememberPagerState(pageCount = { 3 }, initialPage = 3)
        )
    }
}