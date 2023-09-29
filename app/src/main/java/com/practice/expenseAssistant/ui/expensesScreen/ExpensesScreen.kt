package com.practice.expenseAssistant.ui.expensesScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.practice.expenseAssistant.R
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
        onClick = {
            val date = if (it) {
                LocalDate.now().minusMonths(1)
            } else {
                LocalDate.now().plusMonths(1)
            }
            expensesViewModel.getAllTransactionsOfMonth(date)
        },
    )
}

@Composable
private fun ExpensesScreenContent(
    modifier: Modifier,
    uiState: ExpensesScreenUiState,
    onClick: (moveBack: Boolean) -> Unit,
) {
    Column(modifier = modifier) {
        when (uiState) {
            is ExpensesScreenUiState.Loading -> CircularProgressIndicator()
            is ExpensesScreenUiState.Failure -> Text(text = uiState.message)
            is ExpensesScreenUiState.Success -> {
                if (uiState.transactions.isEmpty()) {
                    Text(text = "No Expenses yet!")
                    return
                }
                TransactionsView(
                    date = LocalDate.now(),
                    transactions = uiState.transactions,
                    onClick = onClick,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.screen_content_padding)),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(text = "Total Expense: ${uiState.totalExpense}")
                }
            }
        }
    }
}