package com.practice.expenseAssistant.ui.transactionScreen

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.ui.common.*
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(modifier: Modifier = Modifier, onTransaction: () -> Unit) {
    var amount by remember { mutableStateOf("") }
    var expenseNote by remember { mutableStateOf("") }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = "Category",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
        )
        Divider()
        TextField(
            value = amount,
            onValueChange = { amount = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = { Text(text = "﷼") },
            modifier = Modifier.fillMaxWidth()
        )
        Divider()
        Dropdown(
            modifier = Modifier.fillMaxWidth(),
            data = mapOf("HBL" to "123234235134", "Meezan" to "50387395032"),
            onSelect = {}
        )
        Divider()
        DatePicker(
            modifier = Modifier.fillMaxWidth(),
            onSelect = {}
        )
        Divider()
        TimePicker(
            modifier = Modifier.fillMaxWidth(),
            onSelect = {}
        )
        Divider()
        TextField(
            value = expenseNote,
            onValueChange = { expenseNote = it },
            label = { Text(text = "Expense Note") },
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(dimensionResource(id = R.dimen.screen_content_padding)),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(onClick = { onTransaction() }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.transaction)
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewTransactionScreen() {
    ExpenseAssistantTheme {
        TransactionScreen(modifier = Modifier.fillMaxSize(), onTransaction = {})
    }
}