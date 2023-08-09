package com.practice.expenseAssistant.ui.common

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.data.*
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import com.practice.expenseAssistant.utils.CategoryType
import com.practice.expenseAssistant.utils.ExpenseType
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun CalendarCard(
    indexInList: Int,
    onSelect: (listIndex: Int) -> Unit,
    calendarDateState: CalendarDateModel
) {
    val cardColor = if (calendarDateState.isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else if (calendarDateState.isCurrentMonthDate) {
        Color.Transparent
    } else {
        MaterialTheme.colorScheme.secondary
    }
    val totalExpenseOfDay = calendarDateState.transactionModel.sumOf {
        if (it.categoryType == CategoryType.EXPENSE) it.amount else 0
    }
    val totalIncomeOfDay = calendarDateState.transactionModel.sumOf {
        if (it.categoryType == CategoryType.INCOME) it.amount else 0
    }
    val expenseOpacity = if (totalExpenseOfDay == 0) 0f else 1f
    Box(
        modifier = Modifier
            .width(dimensionResource(id = R.dimen.calendar_card_width))
            .height(dimensionResource(id = R.dimen.calendar_card_height))
            .border(dimensionResource(id = R.dimen.border_stroke), color = Color.Gray)
            .background(color = cardColor)
            .clickable { onSelect(indexInList) },
        contentAlignment = Alignment.BottomStart
    ) {
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            AnimatedVisibility(visible = totalIncomeOfDay > 0) {
                Text(
                    text = totalIncomeOfDay.toString(),
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(id = R.dimen.element_spacing)),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = totalExpenseOfDay.toString(),
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.element_spacing))
                    .alpha(expenseOpacity),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
            )
        }
        Text(
            text = calendarDateState.date.dayOfMonth.toString(),
            modifier = Modifier.padding(dimensionResource(id = R.dimen.element_spacing)),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewCalendarCard() {
    ExpenseAssistantTheme {
        LazyVerticalGrid(columns = GridCells.Fixed(3)) {
            items(6) {
                CalendarCard(
                    indexInList = it,
                    calendarDateState = CalendarDateModel(
                        id = it,
                        date = LocalDate.now().plusDays(it.toLong()),
                        isSelected = it == 3,
                        isCurrentMonthDate = it == 2,
                        transactionModel = listOf(
                            TransactionModel(
                                categoryType = CategoryType.EXPENSE,
                                category = ExpenseType.BILL,
                                note = "Water and pipe maintenance",
                                amount = it * 200,
                                date = LocalDate.now(),
                                time = LocalTime.now()
                            ),
                        )
                    ),
                    onSelect = { }
                )
            }
        }
    }
}