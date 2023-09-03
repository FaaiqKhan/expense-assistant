package com.practice.expenseAssistant.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.library.slide_to_dismiss.SlideToDismiss
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.data.BankAccount
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme

@Composable
fun BriefAccountDetailsCard(
    bankAccount: BankAccount,
    deleteBankAccount: () -> Unit,
    editBankAccount: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        SlideToDismiss(
            modifier = Modifier
                .padding(start = dimensionResource(id = R.dimen.screen_content_padding))
                .padding(vertical = dimensionResource(id = R.dimen.card_padding))
                .fillMaxWidth(),
            data = bankAccount,
            onDismiss = { deleteBankAccount() },
        ) {
            Column {
                Text(text = "Bank name: ${bankAccount.name}")
                Text(text = "Account #: ${bankAccount.number}")
            }
            IconButton(onClick = { editBankAccount() }) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = stringResource(R.string.edit_bank_account_details)
                )
            }
        }
    }
}

@Preview(showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun PreviewBriefAccountDetailsCard() {
    ExpenseAssistantTheme {
        BriefAccountDetailsCard(
            bankAccount = BankAccount(
                name = "Dutches",
                iBan = "ABC-23123012412",
                number = "1234567890",
                balance = 3000.0
            ),
            deleteBankAccount = {},
            editBankAccount = {}
        )
    }
}

