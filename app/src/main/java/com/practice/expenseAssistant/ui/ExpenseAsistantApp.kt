package com.practice.expenseAssistant.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.ui.homeScreen.HomeScreen
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme

@Composable
fun ExpenseAssistantApp(modifier: Modifier = Modifier) {
    HomeScreen(
        homeViewModel = viewModel(), modifier = modifier
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewExpenseAssistantApp() {
    ExpenseAssistantTheme {
        ExpenseAssistantApp(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.screen_content_padding))
        )
    }
}