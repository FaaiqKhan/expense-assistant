package com.practice.expenseAssistant.ui.common

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import java.time.LocalDate

@Composable
fun CalendarCard(date: Int, content: String, isSelected: Boolean, isCurrentMonthDate: Boolean) {
    val cardColor = if (isSelected) {
        Color.Green
    } else if (isCurrentMonthDate) {
        Color.Transparent
    } else {
        Color.LightGray
    }
    Column(
        modifier = Modifier
            .width(dimensionResource(id = R.dimen.calendar_card_width))
            .height(dimensionResource(id = R.dimen.calendar_card_height))
            .border(dimensionResource(id = R.dimen.border_stroke), color = Color.Gray)
            .background(color = cardColor),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = content,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.calendar_padding)),
            style = MaterialTheme.typography.labelSmall,
        )
        Text(
            text = date.toString(),
            modifier = Modifier.padding(dimensionResource(id = R.dimen.calendar_padding)),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewCalendarCard() {
    ExpenseAssistantTheme {
        LazyVerticalGrid(columns = GridCells.Fixed(3)) {
            items(6) {
                CalendarCard(
                    date = LocalDate.now().dayOfWeek.value,
                    content = "${it + 1}k",
                    isSelected = it == 4,
                    isCurrentMonthDate = it == 3,
                )
            }
        }
    }
}