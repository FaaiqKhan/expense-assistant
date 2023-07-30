package com.practice.expenseAssistant.ui.homeScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.ui.common.ShowTotalExpenseCard
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier, userName: String) {
    Column(modifier = modifier) {
        Text(text = "Hi $userName", style = MaterialTheme.typography.displayLarge)
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.element_spacing)))
        ShowTotalExpenseCard(
            totalExpense = 13500.00,
            modifier = Modifier.fillMaxWidth(),
            onClickViewAll = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewHomeScreen() {
    ExpenseAssistantTheme {
        HomeScreen(
            userName = "FaaiqKhan",
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.screen_content_padding))
        )
    }
}