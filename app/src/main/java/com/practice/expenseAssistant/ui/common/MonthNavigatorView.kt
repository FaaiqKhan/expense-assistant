package com.practice.expenseAssistant.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.practice.expenseAssistant.R
import java.time.LocalDate

@Composable
fun MonthNavigatorView(
    modifier: Modifier = Modifier,
    date: LocalDate,
    onClick: (date: LocalDate) -> Unit,
) {
    var localDate by remember { mutableStateOf(date) }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        IconButton(
            onClick = {
                localDate = localDate.minusMonths(1)
                onClick(localDate)
            },
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.previous_month),
            )
        }
        Text(
            text = "${localDate.month} ${localDate.year}",
            style = MaterialTheme.typography.displaySmall
        )
        IconButton(
            onClick = {
                localDate = localDate.plusMonths(1)
                onClick(localDate)
            },
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = stringResource(R.string.next_month),
            )
        }
    }
}