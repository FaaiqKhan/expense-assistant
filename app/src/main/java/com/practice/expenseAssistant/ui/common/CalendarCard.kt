package com.practice.expenseAssistant.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.data.*
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import java.time.LocalDate

@Composable
fun CalendarCard(
    indexInList: Int,
    onSelect: (listIndex: Int) -> Unit,
    calendarDateState: CalendarDateModel
) {
    val expenseOpacity = if (calendarDateState.expenseModel?.expense == null) 0f else 1f
    val cardColor = if (calendarDateState.isSelected) {
        Color.Green
    } else if (calendarDateState.isCurrentMonthDate) {
        Color.Transparent
    } else {
        Color.LightGray
    }
    Column(
        modifier = Modifier
            .width(dimensionResource(id = R.dimen.calendar_card_width))
            .height(dimensionResource(id = R.dimen.calendar_card_height))
            .border(dimensionResource(id = R.dimen.border_stroke), color = Color.Gray)
            .background(color = cardColor)
            .clickable { onSelect(indexInList) },
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = calendarDateState.expenseModel?.expense.toString(),
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.element_spacing))
                .alpha(expenseOpacity),
            style = MaterialTheme.typography.labelSmall,
        )
        Text(
            text = calendarDateState.date.dayOfMonth.toString(),
            modifier = Modifier.padding(dimensionResource(id = R.dimen.element_spacing)),
            style = MaterialTheme.typography.bodyMedium
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
                        expenseModel = if (it == 0) null else ExpenseModel(
                            expenseType = ExpenseType.BILL,
                            expenseNote = "Water and pipe maintenance",
                            expense = it * 200
                        )
                    ),
                    onSelect = { }
                )
            }
        }
    }
}