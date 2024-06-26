package com.practice.expenseAssistant.ui.common

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import java.time.LocalDate

@Composable
fun TotalExpenseCard(
    modifier: Modifier = Modifier,
    totalExpense: Double = 0.0,
    onClick: () -> Unit,
) {
    Card(modifier = modifier) {
        Row(
            modifier = modifier.padding(dimensionResource(id = R.dimen.card_padding)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = if (totalExpense == 0.0) stringResource(R.string.no_expense_yet)
                    else stringResource(id = R.string.euro_symbol, totalExpense),
                    style = MaterialTheme.typography.displayMedium
                )
                Text(
                    text = stringResource(id = R.string.total_expense_till_now),
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Column(
                modifier = Modifier
                    .size(30.dp)
                    .border(1.dp, color = Color.Red)
                    .clickable { onClick() },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Divider(color = Color.Red, modifier = Modifier.padding(top = 5.dp))
                Text(text = LocalDate.now().dayOfMonth.toString())
            }
        }
    }
}

@Preview
@Composable
private fun PreviewShowTotalExpenseCard() {
    ExpenseAssistantTheme {
        TotalExpenseCard(
            modifier = Modifier.fillMaxWidth(),
            totalExpense = 3000.0,
        ) {}
    }
}