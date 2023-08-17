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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    uiState: LoginScreenUiState,
    signIn: (name: String, password: String) -> Unit
) {
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

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
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences
            ),
            singleLine = true,
            label = { Text(text = stringResource(id = R.string.user_name)) },
            isError = uiState is LoginScreenUiState.Failure,
            modifier = Modifier.fillMaxWidth(),
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
                val content: String = if (!showPassword) {
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
            isError = uiState is LoginScreenUiState.Failure,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.screen_content_padding)))
        Button(
            modifier = Modifier.width(dimensionResource(id = R.dimen.button_width)),
            onClick = {
                focusManager.clearFocus()
                signIn(userName, password)
            },
        ) {
            if (uiState is LoginScreenUiState.Loading)
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.inversePrimary,
                    modifier = Modifier.height(
                        dimensionResource(id = R.dimen.circular_indicator_height)
                    )
                )
            else
                Text(text = stringResource(id = R.string.sign_in))
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewSignInScreen() {
    ExpenseAssistantTheme {
        SignInScreen(uiState = LoginScreenUiState.Ideal, signIn = { _, _ -> })
    }
}