package com.practice.expenseAssistant.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme

@Composable
fun OpenCloseBalanceCard(
    modifier: Modifier = Modifier,
    openBalanceOfMonth: Double,
    closeBalanceOfMonth: Double
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.zero)),
    ) {
        Row(modifier = Modifier.padding(dimensionResource(id = R.dimen.card_padding))) {
            Text(
                text = stringResource(id = R.string.opening_balance, openBalanceOfMonth),
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(id = R.string.closing_balance, closeBalanceOfMonth),
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewOpenCloseBalanceCard() {
    ExpenseAssistantTheme {
        OpenCloseBalanceCard(
            modifier = Modifier.fillMaxWidth(),
            openBalanceOfMonth = 1000.0,
            closeBalanceOfMonth = 400.0
        )
    }
}