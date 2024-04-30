package com.practice.expenseAssistant.ui.statementScreen

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.data.TransactionModel
import com.practice.expenseAssistant.ui.common.MonthNavigatorView
import com.practice.expenseAssistant.ui.common.TransactionsView
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import com.practice.expenseAssistant.ui.theme.spacing
import com.practice.expenseAssistant.utils.*
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun StatementScreen(
    modifier: Modifier = Modifier,
    statementViewModel: StatementScreenViewModel = hiltViewModel(),
) {
    val uiState = statementViewModel.uiState.collectAsState()
    StatementScreenContent(
        modifier = modifier,
        uiState = uiState.value,
        selectedMonth = statementViewModel.getSelectedMonth(),
        onClick = { statementViewModel.getAllTransactionsOfMonth(it) },
    )
}

@Composable
private fun StatementScreenContent(
    modifier: Modifier,
    uiState: StatementScreenUiState,
    selectedMonth: LocalDate,
    onClick: (date: LocalDate) -> Unit,
) {
    Column(modifier = modifier) {
        MonthNavigatorView(date = selectedMonth, onClick = onClick)
        when (uiState) {
            is StatementScreenUiState.Loading -> {
                CircularProgressIndicator()
            }

            is StatementScreenUiState.Failure -> {
                Text(text = uiState.message)
            }

            is StatementScreenUiState.Success -> {
                if (uiState.transactions.isEmpty()) {
                    Text(text = stringResource(R.string.no_transactions_yet))
                    return
                }
                TransactionsView(uiState.transactions)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.large),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(R.string.total_income, uiState.totalIncome))
                    Text(text = stringResource(R.string.total_expense, uiState.totalExpense))
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun StatementScreenPreview() {
    ExpenseAssistantTheme {
        StatementScreenContent(
            modifier = Modifier,
            uiState = StatementScreenUiState.Success(
                transactions = listOf(
                    TransactionModel(
                        categoryType = CategoryType.INCOME,
                        category = IncomeType.BUSINESS_INCOME,
                        amount = 3000.0,
                        date = LocalDate.now(),
                        time = LocalTime.now(),
                        note = "Rocket sell",
                        edit = false,
                        month = LocalDate.now().monthValue,
                        year = LocalDate.now().year
                    ),
                    TransactionModel(
                        categoryType = CategoryType.EXPENSE,
                        category = ExpenseType.BABY,
                        amount = 200.0,
                        date = LocalDate.now(),
                        time = LocalTime.now(),
                        note = "Pamper",
                        edit = false,
                        month = LocalDate.now().monthValue,
                        year = LocalDate.now().year
                    ),
                    TransactionModel(
                        categoryType = CategoryType.INCOME,
                        category = IncomeType.OTHERS,
                        amount = 100.0,
                        date = LocalDate.now(),
                        time = LocalTime.now(),
                        note = "Part-time",
                        edit = false,
                        month = LocalDate.now().monthValue,
                        year = LocalDate.now().year
                    )
                ),
                date = LocalDate.now(),
                totalIncome = 3000.0,
                totalExpense = 200.0,
            ),
            onClick = {},
            selectedMonth = LocalDate.now()
        )
    }
}