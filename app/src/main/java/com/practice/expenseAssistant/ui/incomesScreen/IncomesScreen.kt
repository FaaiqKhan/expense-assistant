package com.practice.expenseAssistant.ui.incomesScreen

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
fun IncomesScreen(
    modifier: Modifier = Modifier,
    incomesViewModel: IncomesScreenViewModel = hiltViewModel(),
) {
    val uiState = incomesViewModel.uiState.collectAsState()

    IncomesScreenContent(
        modifier = modifier,
        uiState = uiState.value,
        selectedMonth = incomesViewModel.getSelectedMonth(),
        onClick = { incomesViewModel.getAllTransactionsOfMonth(it) },
    )
}

@Composable
fun IncomesScreenContent(
    modifier: Modifier,
    uiState: IncomesScreenUiState,
    selectedMonth: LocalDate,
    onClick: (date: LocalDate) -> Unit,
) {
    Column(modifier = modifier) {
        MonthNavigatorView(date = selectedMonth, onClick = onClick)
        when (uiState) {
            is IncomesScreenUiState.Loading -> CircularProgressIndicator()
            is IncomesScreenUiState.Failure -> Text(text = uiState.message)
            is IncomesScreenUiState.Success -> {
                if (uiState.transactions.isEmpty()) {
                    Text(text = stringResource(R.string.no_income_yet))
                    return
                }
                TransactionsView(transactions = uiState.transactions)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.screen_content_padding)),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(text = stringResource(R.string.total_income, uiState.totalIncome))
                }
            }
        }
    }
}
