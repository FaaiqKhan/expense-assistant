package com.practice.expenseAssistant.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.data.TransactionModel
import com.practice.expenseAssistant.utils.CategoryType
import java.time.LocalDate

@Composable
fun TransactionsView(
    date: LocalDate,
    transactions: List<TransactionModel>,
    onClick: (goPrevMonth: Boolean) -> Unit,
) {
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
                        Color.Red
                    } else {
                        Color.Unspecified
                    }
                )
            }
        }
    }
}