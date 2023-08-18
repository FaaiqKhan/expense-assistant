package com.practice.expenseAssistant.ui.transactionScreen

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.data.*
import com.practice.expenseAssistant.ui.common.*
import com.practice.expenseAssistant.ui.homeScreen.HomeScreenViewModel
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import com.practice.expenseAssistant.utils.*
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun TransactionScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    expenseAssistant: HomeScreenViewModel,
) {
    val categoryType: Any
    val category = when (expenseAssistant.categoryType) {
        CategoryType.INCOME -> {
            categoryType = CategoryType.INCOME
            (expenseAssistant.category as IncomeType).value
        }

        else -> {
            categoryType = CategoryType.EXPENSE
            (expenseAssistant.category as ExpenseType).value
        }
    }

    TransactionScreenContent(
        modifier = modifier,
        date = expenseAssistant.today,
        categoryType = categoryType,
        category = category,
        user = expenseAssistant.getUser(),
        onNavigate = {
            if (it == null) navController.popBackStack()
            else navController.navigate(it)
        },
        addTransaction = expenseAssistant::addTransaction
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionScreenContent(
    modifier: Modifier,
    user: UserModel,
    date: LocalDate,
    category: Any,
    categoryType: CategoryType,
    onNavigate: (screenName: String?) -> Unit,
    addTransaction: (transaction: TransactionModel, bankAccount: BankAccount) -> Unit
) {

    val maxChar = 100
    val height = dimensionResource(id = R.dimen.field_height)
    val elementSpacing = dimensionResource(id = R.dimen.element_spacing)

    var amount by remember { mutableStateOf("") }
    var expenseNote by remember { mutableStateOf("") }
    var transactionDate by remember { mutableStateOf(date) }
    var transactionTime by remember { mutableStateOf(LocalTime.now()) }
    var bankAccount by remember { mutableStateOf(user.selectedBankAccount) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Category: $category",
                modifier = Modifier
                    .padding(horizontal = dimensionResource(id = R.dimen.screen_content_padding)),
                style = MaterialTheme.typography.headlineLarge,
            )
            TextButton(onClick = { onNavigate(Screens.CATEGORY.name) }) { Text(text = "Change") }
        }
        Spacer(modifier = Modifier.height(elementSpacing))
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            leadingIcon = { Text(text = user.currencyType.icon) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = stringResource(R.string.amount)) },
            singleLine = true,
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.zero))
        )
        Spacer(modifier = Modifier.height(elementSpacing))
        Dropdown(
            modifier = Modifier.fillMaxWidth(),
            data = user.bankAccounts,
            bankAccount = bankAccount,
            onSelect = { bankAccount = it }
        )
        Spacer(modifier = Modifier.height(elementSpacing))
        DatePicker(
            Modifier
                .fillMaxWidth()
                .height(height)
                .padding(horizontal = dimensionResource(id = R.dimen.screen_content_padding)),
            onSelect = { transactionDate = it },
        )
        Spacer(modifier = Modifier.height(elementSpacing))
        Divider()
        TimePicker(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .padding(horizontal = dimensionResource(id = R.dimen.screen_content_padding)),
            onSelect = { transactionTime = it },
        )
        OutlinedTextField(
            value = expenseNote,
            onValueChange = { if (it.length < maxChar) expenseNote = it },
            label = { Text(text = stringResource(R.string.expense_note)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.expense_note_height)),
            supportingText = {
                Text(
                    text = "${expenseNote.length} / $maxChar",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.labelMedium
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.zero))
        )
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(dimensionResource(id = R.dimen.screen_content_padding)),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = {
                    addTransaction(
                        TransactionModel(
                            categoryType = categoryType,
                            category = category,
                            note = expenseNote,
                            amount = amount.toDouble(),
                            date = transactionDate,
                            time = transactionTime
                        ),
                        bankAccount
                    )
                    onNavigate(null)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.transaction)
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun PreviewTransactionScreen() {
    ExpenseAssistantTheme {
        TransactionScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.screen_content_padding)),
            date = LocalDate.now(),
            categoryType = CategoryType.INCOME,
            category = IncomeType.BUSINESS_INCOME,
            user = UserModel(
                name = "Faiq",
                bankAccounts = listOf(
                    BankAccount(
                        name = "Meezan",
                        iBan = "DKLJ#SDKJF#()",
                        number = "1231212312",
                        balance = 30000.0
                    )
                ),
                currencyType = CurrencyType.Dollar,
                selectedBankAccount = BankAccount(
                    name = "Meezan",
                    iBan = "DKLJ#SDKJF#()",
                    number = "1231212312",
                    balance = 30000.0
                ),
                listOf()
            ),
            onNavigate = {},
            addTransaction = {_, _ -> }
        )
    }
}