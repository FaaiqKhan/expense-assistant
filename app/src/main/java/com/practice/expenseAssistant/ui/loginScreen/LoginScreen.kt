package com.practice.expenseAssistant.ui.loginScreen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.utils.Screens

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
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

    if (loginScreenState is LoginScreenState.Success) {
        navController.navigate(Screens.HOME.name) {
            popUpTo(0)
        }
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