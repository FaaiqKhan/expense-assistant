package com.practice.expenseAssistant.ui.expensesScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.ui.common.MonthNavigatorView
import com.practice.expenseAssistant.ui.common.TransactionsView
import java.time.LocalDate

@Composable
fun ExpensesScreen(
    modifier: Modifier = Modifier,
    expensesViewModel: ExpensesScreenViewModel = hiltViewModel(),
) {
    val uiState = expensesViewModel.uiState.collectAsState()

    ExpensesScreenContent(
        modifier = modifier,
        uiState = uiState.value,
        selectedMonth = expensesViewModel.getSelectedMonth(),
        onClick = { expensesViewModel.getAllTransactionsOfMonth(it) },
    )
}

@Composable
private fun ExpensesScreenContent(
    modifier: Modifier,
    uiState: ExpensesScreenUiState,
    selectedMonth: LocalDate,
    onClick: (date: LocalDate) -> Unit,
) {
    Column(modifier = modifier) {
        MonthNavigatorView(date = selectedMonth, onClick = onClick)
        when (uiState) {
            is ExpensesScreenUiState.Loading -> CircularProgressIndicator()
            is ExpensesScreenUiState.Failure -> Text(text = uiState.message)
            is ExpensesScreenUiState.Success -> {
                if (uiState.transactions.isEmpty()) {
                    Text(text = stringResource(R.string.no_expenses_yet))
                    return
                }
                TransactionsView(transactions = uiState.transactions)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.screen_content_padding)),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(text = stringResource(R.string.total_expense, uiState.totalExpense))
                }
            }
        }
    }
}