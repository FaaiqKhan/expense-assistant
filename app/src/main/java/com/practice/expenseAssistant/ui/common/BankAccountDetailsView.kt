package com.practice.expenseAssistant.ui.common

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.data.BankAccount
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankAccountDetailsView(
    modifier: Modifier = Modifier,
    addBankAccount: (bankAccount: BankAccount) -> Unit
) {

    var name by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Note,
                    contentDescription = stringResource(id = R.string.bank_name)
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences
            ),
            singleLine = true,
            label = { Text(text = stringResource(id = R.string.bank_name)) },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.element_spacing)))
        OutlinedTextField(
            value = number,
            onValueChange = { number = it },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Numbers,
                    contentDescription = stringResource(id = R.string.account_number)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            label = { Text(text = stringResource(id = R.string.account_number)) },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.element_spacing)))
        OutlinedTextField(
            value = balance,
            onValueChange = { balance = it },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.AccountBalance,
                    contentDescription = stringResource(id = R.string.account_balance)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            label = { Text(text = stringResource(id = R.string.account_balance)) },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.element_spacing)))
        Button(
            onClick = {
                if (name.isBlank() || number.isBlank() || balance.isBlank()) {
                    Toast.makeText(
                        context,
                        "Field cannot be empty",
                        Toast.LENGTH_LONG
                    ).show()
                    return@Button
                }
                addBankAccount(BankAccount(name, number, balance.toDouble()))
                number = ""
                balance = ""
            },
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(id = R.string.add_bank_account)
            )
            Text(text = stringResource(id = R.string.add_bank_account))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBankAccountDetailsView() {
    ExpenseAssistantTheme {
        BankAccountDetailsView(addBankAccount = {})
    }
}