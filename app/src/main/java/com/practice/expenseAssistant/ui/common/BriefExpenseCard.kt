package com.practice.expenseAssistant.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.data.ExpenseModel
import com.practice.expenseAssistant.utils.ExpenseType
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme

@Composable
fun BriefExpenseCard(modifier: Modifier = Modifier, expenseBriefing: ExpenseModel) {
    Card(modifier = modifier, shape = RoundedCornerShape(dimensionResource(id = R.dimen.zero))) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.card_padding)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = expenseBriefing.expenseNote,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1.7f),
                maxLines = 1,
            )
            Text(
                text = stringResource(id = R.string.euro_symbol, expenseBriefing.expense),
                modifier = Modifier.weight(0.5f),
                textAlign = TextAlign.End
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewBriefExpenseCard() {
    ExpenseAssistantTheme {
        BriefExpenseCard(
            modifier = Modifier.fillMaxWidth(),
            expenseBriefing = ExpenseModel(
                expenseType = ExpenseType.BILL,
                expenseNote = "Computer repair on galaxy computer shop w",
                expense = 3500
            )
        )
    }
}