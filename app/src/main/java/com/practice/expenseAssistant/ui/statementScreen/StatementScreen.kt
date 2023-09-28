package com.practice.expenseAssistant.ui.statementScreen

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.data.TransactionModel
import com.practice.expenseAssistant.ui.common.TransactionsView
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
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
        modifier = modifier, uiState = uiState.value,
        getTotalExpense = statementViewModel::getTotalExpense,
        getTotalIncome = statementViewModel::getTotalIncome,
        onClick = {
            val date = if (it) {
                LocalDate.now().minusMonths(1)
            } else {
                LocalDate.now().plusMonths(1)
            }
            statementViewModel.getAllTransactionsOfMonth(date)
        },
    )
}

@Composable
private fun StatementScreenContent(
    modifier: Modifier,
    uiState: StatementScreenUiState,
    onClick: (moveBack: Boolean) -> Unit,
    getTotalExpense: () -> Double,
    getTotalIncome: () -> Double
) {
    Column(
        modifier = modifier
    ) {
        when (uiState) {
            is StatementScreenUiState.Loading -> {
                CircularProgressIndicator()
            }

            is StatementScreenUiState.Failure -> {
                Text(text = uiState.message)
            }

            is StatementScreenUiState.Success -> {
                if (uiState.transactions.isEmpty()) {
                    Text(text = "No transactions yet!")
                    return
                }
                TransactionsView(LocalDate.now(), uiState.transactions, onClick)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.screen_content_padding)),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Total Income: ${getTotalIncome()}")
                    Text(text = "Total Expense: ${getTotalExpense()}")
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
                listOf(
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
                LocalDate.now()
            ),
            onClick = {},
            getTotalExpense = {
                0.0
            },
            getTotalIncome = { 0.0 }
        )
    }
}