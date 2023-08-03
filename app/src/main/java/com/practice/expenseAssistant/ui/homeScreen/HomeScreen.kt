package com.practice.expenseAssistant.ui.homeScreen

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.ui.common.*
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier, homeViewModel: HomeScreenViewModel) {
    Column(modifier = modifier, horizontalAlignment = Alignment.End) {
        Text(
            text = "Hi ${homeViewModel.userModel.name}",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.screen_content_padding))
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.element_spacing)))
        TotalExpenseCard(
            modifier = Modifier.fillMaxWidth(),
            homeViewModel = homeViewModel,
            onClickViewAll = { }
        )
        CalendarView(homeViewModel = homeViewModel)
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.calendar_padding)))
        OpenCloseBalanceCard(
            modifier = Modifier.fillMaxWidth(),
            openBalance = homeViewModel.monthOpeningBalance,
            closeBalance = homeViewModel.monthClosingBalance,
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewHomeScreen() {
    ExpenseAssistantTheme {
        HomeScreen(
            homeViewModel = viewModel(),
            modifier = Modifier.fillMaxSize()
        )
    }
}