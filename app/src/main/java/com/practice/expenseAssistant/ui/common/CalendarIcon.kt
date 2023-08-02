package com.practice.expenseAssistant.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarIcon(modifier: Modifier = Modifier, date: LocalDate, onClick: () -> Unit) {
    Card(
        modifier = modifier.width(dimensionResource(id = R.dimen.calendar_icon_width)),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.zero)),
        border = BorderStroke(
            width = dimensionResource(id = R.dimen.border_stroke),
            color = Color.Gray
        ),
        onClick = onClick
    ) {
        Text(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.element_spacing))
                .fillMaxWidth(),
            text = date.dayOfMonth.toString(),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewCalendarView() {
    ExpenseAssistantTheme {
        CalendarIcon(date = LocalDate.now()) {}
    }
}