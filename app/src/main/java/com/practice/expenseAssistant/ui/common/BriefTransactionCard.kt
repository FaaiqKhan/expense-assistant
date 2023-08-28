package com.practice.expenseAssistant.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.data.TransactionModel
import com.practice.expenseAssistant.utils.*
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BriefTransactionCard(
    modifier: Modifier = Modifier,
    transaction: TransactionModel,
    onClick: () -> Unit,
) {
    val time = Utils.updateTo2Digits(transaction.time.hour, transaction.time.minute)
    Card(onClick = onClick, shape = RoundedCornerShape(dimensionResource(id = R.dimen.zero))) {
        Row(modifier = modifier.padding(all = dimensionResource(id = R.dimen.card_padding))) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                Text(text = "Amount: ${transaction.amount}")
                if (transaction.note.isNotEmpty()) {
                    Text(
                        text = "Note: ${transaction.note}",
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                }
            }
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.element_spacing)))
            Column {
                Text(text = "Date: ${transaction.date}")
                Text(text = "Time: $time")
            }
        }
    }
}

@Preview(showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun PreviewBriefTransactionCard() {
    BriefTransactionCard(
        transaction = TransactionModel(
            categoryType = CategoryType.EXPENSE,
            category = ExpenseType.BILL,
            note = "Electricity bill with Gas bill and Water bill",
            amount = 100.00,
            date = LocalDate.now(),
            time = LocalTime.now(),
        )
    ) {}
}