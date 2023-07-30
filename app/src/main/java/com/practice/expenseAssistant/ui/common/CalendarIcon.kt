package com.practice.expenseAssistant.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import java.time.LocalDate

@Composable
fun CalendarIcon(modifier: Modifier = Modifier, date: LocalDate) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.zero)),
        border = BorderStroke(
            width = dimensionResource(id = R.dimen.border_stroke),
            color = Color.Gray
        )
    ) {
        Text(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.element_spacing)),
            text = date.dayOfMonth.toString()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCalendarView() {
    ExpenseAssistantTheme {
        CalendarIcon(date = LocalDate.now())
    }
}