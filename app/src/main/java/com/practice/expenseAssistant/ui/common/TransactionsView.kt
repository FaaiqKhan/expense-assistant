package com.practice.expenseAssistant.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.practice.expenseAssistant.data.TransactionModel
import com.practice.expenseAssistant.utils.CategoryType

@Composable
fun TransactionsView(transactions: List<TransactionModel>) {
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
                        Color.Red
                    } else {
                        Color.Unspecified
                    }
                )
            }
        }
    }
}