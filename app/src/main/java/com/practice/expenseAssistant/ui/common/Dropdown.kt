package com.practice.expenseAssistant.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Dropdown(
    modifier: Modifier = Modifier,
    data: Map<String, String>,
    onSelect: (accountDetails: Map.Entry<String, String>) -> Unit
) {
    val selectedText by remember { mutableStateOf(data.values.first()) }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        )
        {
            data.forEach { item ->
                DropdownMenuItem(
                    content = { Text(text = item.value) },
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
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
            data = mapOf("HBL" to "123234235134", "Meezan" to "50387395032"),
            onSelect = {}
        )
    }
}