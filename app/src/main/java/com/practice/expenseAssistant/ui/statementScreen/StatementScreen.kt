package com.practice.expenseAssistant.ui.statementScreen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.data.TransactionModel
import com.practice.expenseAssistant.ui.common.TableCell
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
                StatementView(
                    transactions = uiState.transactions,
                    onClick = onClick,
                )
            }
        }
    }
}

@Composable
private fun StatementView(
    transactions: List<TransactionModel>,
    onClick: (moveBack: Boolean) -> Unit,
) {
    val date = LocalDate.now()
    if (transactions.isEmpty()) {
        Text(text = "No transactions yet!")
    } else {
        var totalIncome by remember { mutableStateOf(0.0) }
        var totalExpense by remember { mutableStateOf(0.0) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(onClick = { onClick(true) }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.previous_month),
                )
            }
            Text(
                text = "${date.month} ${date.year}",
                style = MaterialTheme.typography.displaySmall
            )
            IconButton(onClick = { onClick(false) }) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = stringResource(R.string.next_month),
                )
            }
        }
        LazyColumn {
            item {
                Row(
                    modifier = Modifier.background(color = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    TableCell(text = "Transaction details", weight = 0.65f)
                    TableCell(text = "Day", weight = 0.12f)
                    TableCell(text = "Amount", weight = 0.23f)
                }
            }
            items(transactions) {
                Row {
                    TableCell(text = it.note, weight = 0.65f)
                    TableCell(text = it.date.dayOfMonth.toString(), weight = 0.12f)
                    TableCell(
                        text = it.amount.toString(),
                        weight = 0.23f,
                        color = if (it.categoryType == CategoryType.EXPENSE) {
                            totalExpense += it.amount
                            Color.Red
                        } else {
                            totalIncome += it.amount
                            Color.Unspecified
                        }
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.screen_content_padding)),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Total Income: $totalIncome")
                    Text(text = "Total Expense: $totalExpense")
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
            )
        ) {}
    }
}