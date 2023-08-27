package com.practice.expenseAssistant.ui.loginScreen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.data.BankAccount
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import com.practice.expenseAssistant.utils.Screens

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    loginViewModel: LoginScreenViewModel = hiltViewModel()
) {
    var isSignUp by remember { mutableStateOf(false) }
    val uiState by loginViewModel.loginScreenUiState.collectAsState()

    if (uiState is LoginScreenUiState.Failure) {
        Toast.makeText(
            LocalContext.current,
            (uiState as LoginScreenUiState.Failure).msg,
            Toast.LENGTH_LONG
        ).show()
    }

    if (uiState is LoginScreenUiState.Success) {
        LaunchedEffect(key1 = Screens.HOME.name) {
            navController.navigate(Screens.HOME.name) { popUpTo(0) }
        }
    }

    LoginScreenContent(
        modifier = modifier,
        isSignUp = isSignUp,
        loginScreenUiState = uiState,
        signIn = loginViewModel::signIn,
        signUp = loginViewModel::signUp,
        onClick = { isSignUp = !isSignUp },
    )
}

@Composable
private fun LoginScreenContent(
    modifier: Modifier,
    isSignUp: Boolean,
    loginScreenUiState: LoginScreenUiState,
    signIn: (name: String, password: String) -> Unit,
    signUp: (
        name: String,
        password: String,
        bankAccounts: List<BankAccount>,
        selectedBankAccount: BankAccount,
    ) -> Unit,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedVisibility(visible = isSignUp.not()) {
            Text(
                text = stringResource(R.string.hello_there),
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(
                    bottom = dimensionResource(id = R.dimen.field_height)
                ),
            )
        }
        AnimatedVisibility(visible = isSignUp) {
            SignUpScreen(uiState = loginScreenUiState, signUp = signUp)
        }
        AnimatedVisibility(visible = isSignUp.not()) {
            SignInScreen(uiState = loginScreenUiState, signIn = signIn)
        }
        TextButton(onClick = onClick) {
            Text(text = stringResource(id = if (isSignUp) R.string.sign_in else R.string.sign_up))
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun PreviewLoginScreen() {
    ExpenseAssistantTheme {
        LoginScreenContent(
            modifier = Modifier,
            isSignUp = false,
            loginScreenUiState = LoginScreenUiState.Ideal,
            signIn = { _, _ -> },
            signUp = { _, _, _, _ -> },
            onClick = {},
        )
    }
}