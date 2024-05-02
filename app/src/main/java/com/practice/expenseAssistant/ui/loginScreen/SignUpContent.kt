package com.practice.expenseAssistant.ui.loginScreen

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
import com.library.slide_to_dismiss.SlideToDismiss
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.data.BankAccount
import com.practice.expenseAssistant.ui.common.BankAccountDetailsView
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import com.practice.expenseAssistant.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    uiState: LoginScreenUiState,
    signUp: (name: String, password: String, bankAccount: BankAccount?) -> Unit
) {

    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var bankAccount by remember { mutableStateOf<BankAccount?>(null) }

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
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.element_spacing)))
        if (bankAccount != null) {
            Text(
                text = "Bank account",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineMedium,
            )
            SlideToDismiss(
                data = bankAccount,
                icon = Icons.Filled.Delete,
                onDismiss = { bankAccount = null },
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Bank Name: ${bankAccount!!.name}",
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.card_padding))
                            .padding(top = dimensionResource(id = R.dimen.card_padding))
                    )
                    Text(
                        text = "Account #: ${bankAccount!!.number}",
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.card_padding))
                            .padding(bottom = dimensionResource(id = R.dimen.card_padding))
                    )
                }
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Divider(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.account_details), modifier = Modifier.padding(
                        horizontal = dimensionResource(
                            id = R.dimen.six_dp
                        )
                    )
                )
                Divider(modifier = Modifier.weight(1f))
            }
            BankAccountDetailsView(
                modifier = modifier,
                addBankAccount = {
                    bankAccount = it
                    focusManager.clearFocus()
                },
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
        }
        Button(
            modifier = Modifier.width(dimensionResource(id = R.dimen.button_width)),
            onClick = { signUp(userName, password, bankAccount) },
        ) {
            if (uiState is LoginScreenUiState.Loading)
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.inversePrimary,
                    modifier = Modifier.size(
                        dimensionResource(id = R.dimen.circular_indicator_height)
                    )
                )
            else
                Text(text = stringResource(id = R.string.sign_up))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSignUpScreen() {
    ExpenseAssistantTheme {
        SignUpScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.element_spacing)),
            uiState = LoginScreenUiState.Success,
            signUp = { _, _, _ -> })
    }
}