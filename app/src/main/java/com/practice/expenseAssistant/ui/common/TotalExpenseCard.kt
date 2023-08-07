package com.practice.expenseAssistant.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.ui.homeScreen.ExpenseAssistantViewModel
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme

@Composable
fun TotalExpenseCard(
    modifier: Modifier = Modifier,
    expenseAssistant: ExpenseAssistantViewModel,
    onClickViewAll: () -> Unit,
) {
    val totalExpense by expenseAssistant.totalExpenseOfMonthState.collectAsState()
    Card(modifier = modifier, shape = RoundedCornerShape(dimensionResource(id = R.dimen.zero))) {
        Row(
            modifier = modifier.padding(dimensionResource(id = R.dimen.card_padding)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = if (totalExpense == 0) stringResource(R.string.no_expense_yet)
                    else stringResource(id = R.string.euro_symbol, totalExpense),
                    style = MaterialTheme.typography.displayMedium
                )
                Text(
                    text = stringResource(id = R.string.total_expense_till_now),
                    style = MaterialTheme.typography.labelMedium
                )
            }
            TextButton(onClick = onClickViewAll) {
                Text(
                    text = stringResource(id = R.string.view_all),
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewShowTotalExpenseCard() {
    ExpenseAssistantTheme {
        TotalExpenseCard(
            modifier = Modifier.fillMaxWidth(),
            expenseAssistant = viewModel(),
            onClickViewAll = {}
        )
    }
}