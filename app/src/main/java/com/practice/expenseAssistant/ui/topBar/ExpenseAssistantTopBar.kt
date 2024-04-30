package com.practice.expenseAssistant.ui.topBar

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.data.TransactionModel
import com.practice.expenseAssistant.ui.common.NoRippleButton
import com.practice.expenseAssistant.utils.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseAssistantTopBar(
    modifier: Modifier = Modifier,
    screen: Screens,
    controller: NavHostController,
) {
    var isMenuOpen by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(id = screen.title),
                style = MaterialTheme.typography.displayLarge
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondary
        ),
        navigationIcon = {
            if (controller.previousBackStackEntry != null) {
                IconButton(onClick = {
                    if (screen == Screens.TRANSACTION) {
                        controller.previousBackStackEntry?.savedStateHandle
                            ?.remove<TransactionModel>("transaction")
                    }
                    controller.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            if (screen == Screens.HOME) {
                IconButton(onClick = { controller.navigate(Screens.CATEGORY.name) }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.add_expense),
                    )
                }
                IconButton(onClick = { isMenuOpen = !isMenuOpen }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = stringResource(id = R.string.menu),
                    )
                }
                DropdownMenu(expanded = isMenuOpen, onDismissRequest = { isMenuOpen = false }) {
                    NoRippleButton(
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.eight_dp))
                            .padding(bottom = dimensionResource(id = R.dimen.eight_dp)),
                        text = R.string.statements
                    ) {
                        isMenuOpen = false
                        controller.navigate(Screens.MONTHLY_STATEMENT.name)
                    }
                    NoRippleButton(
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.eight_dp))
                            .padding(bottom = dimensionResource(id = R.dimen.eight_dp)),
                        text = R.string.view_all_expenses,
                    ) {
                        isMenuOpen = false
                        controller.navigate(Screens.MONTHLY_EXPENSES.name)
                    }
                    NoRippleButton(
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.eight_dp))
                            .padding(bottom = dimensionResource(id = R.dimen.eight_dp)),
                        text = R.string.view_all_income,
                    ) {
                        isMenuOpen = false
                        controller.navigate(Screens.MONTHLY_INCOMES.name)
                    }
                    NoRippleButton(
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.eight_dp))
                            .padding(bottom = dimensionResource(id = R.dimen.eight_dp)),
                        text = R.string.profile,
                    ) {
                        isMenuOpen = false
                        controller.navigate(Screens.PROFILE.name)
                    }
                    NoRippleButton(
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.eight_dp)),
                        text = R.string.about,
                    ) {
                        isMenuOpen = false
                        controller.navigate(Screens.ABOUT.name)
                    }
                }
            }
        }
    )
}