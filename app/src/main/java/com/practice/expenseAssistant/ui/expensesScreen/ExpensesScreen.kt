package com.practice.expenseAssistant.ui.expensesScreen

import android.content.res.Configuration
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme

@Composable
fun ExpensesScreen(modifier: Modifier = Modifier) {
    CircularProgressIndicator()
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun ExpensesScreenPreview() {
    ExpenseAssistantTheme {
        ExpensesScreen()
    }
}