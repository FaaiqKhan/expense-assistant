package com.practice.expenseAssistant.ui.loginScreen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginScreenViewModel = hiltViewModel()
) {
    val loginScreenState by loginViewModel.loginScreenState.collectAsState()
    var isSignUp by remember { mutableStateOf(true) }

    if (loginScreenState is LoginScreenState.Failure) {
        Toast.makeText(
            LocalContext.current,
            (loginScreenState as LoginScreenState.Failure).msg,
            Toast.LENGTH_LONG
        ).show()
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedVisibility(visible = !isSignUp) {
            SignUpScreen(
                uiState = loginScreenState,
                signUp = { name, password, bankAccount ->
                    loginViewModel.signUp(
                        name,
                        password,
                        bankAccount
                    )
                },
            )
        }
        AnimatedVisibility(visible = isSignUp) {
            SignInScreen(
                uiState = loginScreenState,
                signIn = { name, password -> loginViewModel.signIn(name, password) },
            )
        }
        val textId = if (isSignUp) R.string.sign_up else R.string.sign_in
        TextButton(onClick = { isSignUp = !isSignUp }) {
            Text(text = stringResource(id = textId))
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewLoginScreen() {
    ExpenseAssistantTheme {
        LoginScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.screen_content_padding))
        )
    }
}