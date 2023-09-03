package com.practice.expenseAssistant.ui.profileScreen

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.DrawerValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.data.BankAccount
import com.practice.expenseAssistant.data.UserModel
import com.practice.expenseAssistant.ui.common.BankAccountDetailsView
import com.practice.expenseAssistant.ui.common.BriefAccountDetailsCard
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import com.practice.expenseAssistant.utils.CurrencyType
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileScreenViewModel = hiltViewModel(),
) {
    ProfileScreenContent(modifier = modifier, user = profileViewModel.getUser())
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun ProfileScreenContent(modifier: Modifier, user: UserModel) {
    var userName by remember { mutableStateOf(user.name) }
    var password by remember { mutableStateOf("abc") }
    val editProfile by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = { BankAccountDetailsView(addBankAccount = { _, _, _, _ -> }) }) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = stringResource(R.string.profile_picture),
                modifier = Modifier
                    .border(
                        width = dimensionResource(id = R.dimen.border_stroke),
                        color = Color.Gray,
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.circle))
                    )
                    .size(150.dp),
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.card_padding)))
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(id = R.dimen.element_spacing)),
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                readOnly = !editProfile,
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
                modifier = Modifier.fillMaxWidth()

            )
            Text(
                text = stringResource(id = R.string.bank_accounts),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(id = R.dimen.element_spacing)),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.displayLarge
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.element_spacing)),
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = dimensionResource(id = R.dimen.element_spacing)),
            ) {
                items(user.bankAccounts) {
                    BriefAccountDetailsCard(
                        bankAccount = it,
                        deleteBankAccount = {},
                        editBankAccount = {},
                    )
                }
            }
            Button(onClick = { scope.launch { sheetState.show() } }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add_bank_account)
                )
                Text(text = stringResource(id = R.string.add_bank_account))
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun PreviewProfileScreen() {
    val bankAccount = BankAccount(
        name = "Dutches",
        iBan = "ABC-23123012412",
        number = "1234567890",
        balance = 3000.0
    )
    ExpenseAssistantTheme {
        ProfileScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = dimensionResource(id = R.dimen.screen_content_padding)),
            user = UserModel(
                name = "Faiq",
                bankAccounts = listOf(
                    bankAccount,
                    bankAccount,
                    bankAccount,
                    bankAccount,
                    bankAccount,
                    bankAccount,
                    bankAccount,
                    bankAccount,
                ),
                currencyType = CurrencyType.Dollar,
                selectedBankAccount = bankAccount
            )
        )
    }
}

