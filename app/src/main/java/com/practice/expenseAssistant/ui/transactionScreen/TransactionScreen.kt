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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.data.TransactionModel
import com.practice.expenseAssistant.ui.common.*
import com.practice.expenseAssistant.ui.homeScreen.ExpenseAssistantViewModel
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import com.practice.expenseAssistant.utils.*
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    modifier: Modifier = Modifier,
    expenseAssistant: ExpenseAssistantViewModel,
    navController: NavHostController,
) {
    var amount by remember { mutableStateOf("") }
    var expenseNote by remember { mutableStateOf("") }
    var transactionDate by remember { mutableStateOf(expenseAssistant.today) }
    var transactionTime by remember { mutableStateOf(LocalTime.now()) }

    val maxChar = 100
    val height = dimensionResource(id = R.dimen.field_height)
    val elementSpacing = dimensionResource(id = R.dimen.element_spacing)

    val category = when (expenseAssistant.categoryType) {
        CategoryType.INCOME -> expenseAssistant.category as IncomeType
        else -> expenseAssistant.category as ExpenseType
    }

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
                style = MaterialTheme.typography.displayMedium,
            )
            TextButton(
                onClick = { navController.navigate(Screens.CATEGORY.name) }
            ) { Text(text = "Change") }
        }
        Spacer(modifier = Modifier.height(elementSpacing))
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            leadingIcon = { Text(text = expenseAssistant.currencyType) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = stringResource(R.string.amount)) },
            singleLine = true,
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.zero))
        )
        Spacer(modifier = Modifier.height(elementSpacing))
        Dropdown(
            modifier = Modifier.fillMaxWidth(),
            data = expenseAssistant.backAccounts,
            onSelect = {}
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
                    expenseAssistant.addTransaction(
                        TransactionModel(
                            categoryType = expenseAssistant.categoryType,
                            category = category,
                            note = expenseNote,
                            amount = amount.toInt(),
                            date = transactionDate,
                            time = transactionTime
                        )
                    )
                    navController.popBackStack()
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
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewTransactionScreen() {
    ExpenseAssistantTheme {
        TransactionScreen(
            modifier = Modifier.fillMaxSize(),
            expenseAssistant = viewModel(),
            navController = rememberNavController(),
        )
    }
}