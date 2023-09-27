package com.practice.expenseAssistant.ui.transactionScreen

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.data.*
import com.practice.expenseAssistant.ui.common.*
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import com.practice.expenseAssistant.utils.*
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun TransactionScreen(
    modifier: Modifier = Modifier,
    transactionViewModel: TransactionScreenViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val transaction: TransactionModel? =
        navController.previousBackStackEntry?.savedStateHandle?.get("transaction")

    TransactionScreenContent(
        modifier = modifier,
        user = transactionViewModel.getUser(),
        transaction = transaction?.copy(edit = true) ?: TransactionModel(
            categoryType = transactionViewModel.getCategoryType(),
            category = transactionViewModel.getCategory(),
            amount = 0.0,
            date = transactionViewModel.getSelectedDate(),
            time = LocalTime.now(),
            month = LocalDate.now().monthValue,
            year = LocalDate.now().year
        ),
        addTransaction = { tran, account ->
            transactionViewModel.addTransaction(tran, account)
            navController.popBackStack()
        },
        removeTransaction = {
            navController.previousBackStackEntry?.savedStateHandle
                ?.remove<TransactionModel>("transaction")
            transactionViewModel.removeTransaction(it)
            navController.popBackStack()
        },
        moveToCategoryScreen = {
            navController.navigate(Screens.CATEGORY.name)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionScreenContent(
    modifier: Modifier,
    user: UserModel,
    transaction: TransactionModel,
    addTransaction: (transaction: TransactionModel, bankAccount: BankAccount) -> Unit,
    removeTransaction: (transaction: TransactionModel) -> Unit,
    moveToCategoryScreen: () -> Unit
) {

    val maxChar = 100
    val height = dimensionResource(id = R.dimen.field_height)
    val elementSpacing = dimensionResource(id = R.dimen.element_spacing)

    var validateField by remember { mutableStateOf(false) }

    var amount by remember { mutableStateOf(transaction.amount.toString()) }
    var bankAccount by remember { mutableStateOf(user.selectedBankAccount) }
    var transactionDate by remember { mutableStateOf(transaction.date) }
    var transactionTime by remember { mutableStateOf(LocalTime.now()) }
    var expenseNote by remember { mutableStateOf(transaction.note) }

    Column(modifier = modifier, horizontalAlignment = Alignment.End) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Category: ${transaction.category}",
                modifier = Modifier
                    .padding(horizontal = dimensionResource(id = R.dimen.screen_content_padding)),
                style = MaterialTheme.typography.headlineLarge,
            )
            Row {
                TextButton(onClick = moveToCategoryScreen) { Text(text = "Change") }
                AnimatedVisibility(visible = transaction.edit) {
                    IconButton(onClick = { removeTransaction(transaction) }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            tint = MaterialTheme.colorScheme.error,
                            contentDescription = stringResource(id = R.string.delete_transaction)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(elementSpacing))
        OutlinedTextField(
            value = if (amount == "0.0") "" else amount,
            onValueChange = { amount = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            leadingIcon = { Text(text = user.currencyType.icon) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = stringResource(R.string.amount)) },
            singleLine = true,
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.zero)),
            isError = validateField && amount.isEmpty()
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
            date = transaction.date,
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
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.zero)),
        )
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(dimensionResource(id = R.dimen.screen_content_padding)),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = {
                    validateField = true
                    if (amount.isEmpty()) return@FloatingActionButton
                    addTransaction(
                        TransactionModel(
                            categoryType = transaction.categoryType,
                            category = transaction.category,
                            note = expenseNote,
                            amount = amount.toDouble(),
                            date = transactionDate,
                            time = transactionTime,
                            month = LocalDate.now().monthValue,
                            year = LocalDate.now().year
                        ),
                        bankAccount
                    )
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
            user = UserModel(
                id = 1,
                name = "Faiq",
                bankAccounts = listOf(
                    BankAccount(
                        name = "ABC",
                        iBan = "DKL-325546A55MBA",
                        number = "1231212312",
                        balance = 30000.0
                    )
                ),
                currencyType = CurrencyType.Dollar,
                selectedBankAccount = BankAccount(
                    name = "XYZ",
                    iBan = "DKJ-325546A55MBA",
                    number = "1231212312",
                    balance = 6000.0
                ),
            ),
            transaction = TransactionModel(
                categoryType = CategoryType.EXPENSE,
                category = ExpenseType.BILL.value,
                amount = 0.0,
                date = LocalDate.now(),
                time = LocalTime.now(),
                edit = true,
                month = LocalDate.now().monthValue,
                year = LocalDate.now().year
            ),
            addTransaction = { _, _ -> },
            removeTransaction = { _ -> },
            moveToCategoryScreen = {}
        )
    }
}