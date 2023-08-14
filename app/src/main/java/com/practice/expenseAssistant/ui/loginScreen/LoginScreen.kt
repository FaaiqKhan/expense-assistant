package com.practice.expenseAssistant.ui.loginScreen

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginScreenViewModel
) {
    val loginScreenState by loginViewModel.loginScreenState.collectAsState()

    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = stringResource(id = R.string.user_name)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            label = { Text(text = stringResource(id = R.string.user_name)) },
            isError = loginScreenState is LoginScreenState.Failure
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.element_spacing)))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = stringResource(id = R.string.password)
                )
            },
            trailingIcon = {
                lateinit var image: ImageVector
                val content: String = if (showPassword) {
                    image = Icons.Filled.VisibilityOff
                    stringResource(id = R.string.show_password)
                } else {
                    image = Icons.Filled.Visibility
                    stringResource(id = R.string.hide_password)
                }
                Icon(
                    imageVector = image,
                    contentDescription = content,
                    modifier = Modifier.clickable { showPassword = !showPassword },
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (showPassword) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            singleLine = true,
            label = { Text(text = stringResource(id = R.string.password)) },
            isError = loginScreenState is LoginScreenState.Failure
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.screen_content_padding)))
        Row {
            if (loginScreenState is LoginScreenState.Loading) {
                CircularProgressIndicator()
            } else {
                Button(onClick = { loginViewModel.signIn(userName, password) }) {
                    Text(text = stringResource(id = R.string.sign_in))
                }
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.card_padding)))
                Button(onClick = { loginViewModel.signIn(userName, password) }) {
                    Text(text = stringResource(id = R.string.sign_up))
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewLoginScreen() {
    ExpenseAssistantTheme {
        LoginScreen(modifier = Modifier.fillMaxSize(), hiltViewModel())
    }
}