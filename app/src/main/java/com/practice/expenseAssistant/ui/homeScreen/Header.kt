package com.practice.expenseAssistant.ui.homeScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.ui.common.NoRippleButton
import com.practice.expenseAssistant.ui.theme.NoRippleTheme
import com.practice.expenseAssistant.ui.theme.spacing
import com.practice.expenseAssistant.utils.Screens

@Composable
fun ScreenHeader(userName: String, controller: NavHostController) {
    var isMenuOpen by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Hi $userName",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier
                .padding(horizontal = MaterialTheme.spacing.large)
        )
        Row {
            CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
                IconButton(onClick = { controller.navigate(Screens.CATEGORY.name) }) {
                    Icon(
                        tint = Color.Red,
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.add_expense),
                    )
                }
                IconButton(onClick = { isMenuOpen = !isMenuOpen }) {
                    Icon(
                        tint = Color.Red,
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = stringResource(id = R.string.menu),
                    )
                }
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
}