package com.practice.expenseAssistant.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.data.BankAccount
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Dropdown(
    modifier: Modifier = Modifier,
    data: List<BankAccount>,
    bankAccount: BankAccount,
    onSelect: (account: BankAccount) -> Unit
) {
    var account by remember { mutableStateOf(bankAccount) }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            value = "${account.name}: ${account.number}",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.zero))
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        )
        {
            data.forEach { item ->
                DropdownMenuItem(
                    content = { Text(text = "${item.name}: ${item.number}") },
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        account = item
                        onSelect(item)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDropdown() {
    ExpenseAssistantTheme {
        Dropdown(
            modifier = Modifier,
            data = listOf(
                BankAccount(
                    name = "Habib Bank Limited",
                    number = "0010031239412830",
                    balance = 60000.00
                ),
                BankAccount(
                    name = "Mezzanine Bank Limited",
                    number = "00200395038219",
                    balance = 160000.00
                ),
            ),
            bankAccount = BankAccount(
                name = "Mezzanine Bank Limited",
                number = "00200395038219",
                balance = 160000.00
            ),
            onSelect = {}
        )
    }
}