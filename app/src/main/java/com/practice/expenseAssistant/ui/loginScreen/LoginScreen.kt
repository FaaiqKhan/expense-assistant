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
import com.practice.expenseAssistant.ui.theme.*
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
        uiState = uiState,
        signIn = loginViewModel::signIn,
        signUp = loginViewModel::signUp,
        onClick = {
            loginViewModel.updateState(LoginScreenUiState.Ideal)
            isSignUp = !isSignUp
        },
    )
}

@Composable
private fun LoginScreenContent(
    modifier: Modifier,
    isSignUp: Boolean,
    uiState: LoginScreenUiState,
    signIn: (name: String, password: String) -> Unit,
    signUp: (
        name: String,
        password: String,
        bankAccount: BankAccount?,
    ) -> Unit,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = Typography.displayLarge,
        )
        Text(
            text = stringResource(R.string.spend_wisely),
            style = Typography.displaySmall,
            modifier = Modifier.padding(
                bottom = dimensionResource(
                    id = R.dimen.six_dp
                )
            )
        )
        AnimatedVisibility(visible = isSignUp.not()) {
            SignInScreen(uiState = uiState, signIn = signIn)
        }
        AnimatedVisibility(visible = isSignUp) {
            SignUpScreen(uiState = uiState, signUp = signUp)
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
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = MaterialTheme.spacing.large)
        ) {
            LoginScreenContent(
                modifier = Modifier,
                isSignUp = false,
                uiState = LoginScreenUiState.Ideal,
                signIn = { _, _ -> },
                signUp = { _, _, _, -> },
                onClick = {},
            )
        }
    }
}