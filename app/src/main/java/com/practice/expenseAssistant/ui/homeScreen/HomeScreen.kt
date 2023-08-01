package com.practice.expenseAssistant.ui.homeScreen

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.ui.common.*
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import java.time.LocalDate

@Composable
fun HomeScreen(modifier: Modifier = Modifier, userName: String) {
    Column(modifier = modifier) {
        Text(text = "Hi $userName", style = MaterialTheme.typography.displayLarge)
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.element_spacing)))
        TotalExpenseCard(
            totalExpense = 13500.00,
            modifier = Modifier.fillMaxWidth(),
            onClickViewAll = { }
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.element_spacing)))
        CalendarView(localDate = LocalDate.now())
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.element_spacing)))
        OpenCloseBalanceCard(
            modifier = Modifier.fillMaxWidth(),
            openBalance = 123,
            closeBalance = 123
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
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