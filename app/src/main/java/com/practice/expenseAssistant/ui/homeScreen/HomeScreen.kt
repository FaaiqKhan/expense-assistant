package com.practice.expenseAssistant.ui.homeScreen

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.data.*
import com.practice.expenseAssistant.ui.common.*
import com.practice.expenseAssistant.ui.theme.NoRippleTheme
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
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
        TotalExpenseCard(
            modifier = Modifier.fillMaxWidth(),
            totalExpense = monthCashFlow.expense,
            onClick = {},
        )
        when (uiState) {
            is HomeScreenUiState.Loading -> CircularProgressIndicator()
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
        Divider()
        CalenderNavigator(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(id = R.dimen.element_spacing))
                .padding(horizontal = dimensionResource(id = R.dimen.screen_content_padding)),
            currentDate = currentMonth, viewMonth = viewMonth,
        )
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
fun CalenderNavigator(
    modifier: Modifier,
    currentDate: LocalDate,
    viewMonth: (previous: Boolean) -> Unit
) {
    val isCurrentMonth = currentDate.month == LocalDate.now().month
        && currentDate.year == LocalDate.now().year
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = MutableInteractionSource()
            ) { viewMonth(true) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(dimensionResource(id = R.dimen.icon_18_dp)),
                imageVector = Icons.Default.ArrowBackIos,
                contentDescription = stringResource(id = R.string.previous_month),
            )
            Text(
                text = Utils.decapitalizeStringExpectFirstLetter(
                    currentDate.minusMonths(1).month.name.substring(0, 3)
                ),
                style = MaterialTheme.typography.headlineLarge,
            )
        }
        Text(
            text = "${Utils.decapitalizeStringExpectFirstLetter(currentDate.month.name)} ${currentDate.year}",
            style = MaterialTheme.typography.headlineMedium
        )
        Row(
            modifier = Modifier
                .alpha(if (isCurrentMonth) 0f else 1.0f)
                .clickable(
                    indication = null,
                    interactionSource = MutableInteractionSource()
                ) { if (!isCurrentMonth) viewMonth(false) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = Utils.decapitalizeStringExpectFirstLetter(
                    currentDate.plusMonths(1).month.name.substring(0, 3)
                ),
                style = MaterialTheme.typography.headlineLarge,
            )
            Icon(
                modifier = Modifier.size(dimensionResource(id = R.dimen.icon_18_dp)),
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = stringResource(id = R.string.next_month),
            )
        }
    }
}

@Composable
fun ScreenHeader(userName: String, controller: NavHostController) {
    var isMenuOpen by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Hi $userName",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.screen_content_padding))
        )
        Row {
            CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
                IconButton(onClick = { controller.navigate(Screens.CATEGORY.name) }) {
                    Icon(
                        tint = Color.Red,
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.add_expense),
                    )
                }
                IconButton(onClick = { isMenuOpen = !isMenuOpen }) {
                    Icon(
                        tint = Color.Red,
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = stringResource(id = R.string.menu),
                    )
                }
            }
            DropdownMenu(expanded = isMenuOpen, onDismissRequest = { isMenuOpen = false }) {
                NoRippleButton(
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = R.dimen.eight_dp))
                        .padding(bottom = dimensionResource(id = R.dimen.eight_dp)),
                    text = R.string.statements
                ) {
                    isMenuOpen = false
                    controller.navigate(Screens.MONTHLY_STATEMENT.name)
                }
                NoRippleButton(
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = R.dimen.eight_dp))
                        .padding(bottom = dimensionResource(id = R.dimen.eight_dp)),
                    text = R.string.view_all_expenses,
                ) {
                    isMenuOpen = false
                    controller.navigate(Screens.MONTHLY_EXPENSES.name)
                }
                NoRippleButton(
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = R.dimen.eight_dp))
                        .padding(bottom = dimensionResource(id = R.dimen.eight_dp)),
                    text = R.string.view_all_income,
                ) {
                    isMenuOpen = false
                    controller.navigate(Screens.MONTHLY_INCOMES.name)
                }
                NoRippleButton(
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = R.dimen.eight_dp))
                        .padding(bottom = dimensionResource(id = R.dimen.eight_dp)),
                    text = R.string.profile,
                ) {
                    isMenuOpen = false
                    controller.navigate(Screens.PROFILE.name)
                }
                NoRippleButton(
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = R.dimen.eight_dp)),
                    text = R.string.about,
                ) {
                    isMenuOpen = false
                    controller.navigate(Screens.ABOUT.name)
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun PreviewHomeScreen() {
    val date = LocalDate.now()
    ExpenseAssistantTheme {
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