package com.practice.expenseAssistant.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.data.CalendarDateModel
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import com.practice.expenseAssistant.ui.theme.spacing
import com.practice.expenseAssistant.utils.Utils
import java.time.LocalDate

@Composable
fun CalendarView(
    modifier: Modifier = Modifier,
    calendar: List<CalendarDateModel>,
    currentDate: LocalDate,
    updateDate: (index: Int) -> Unit,
    viewMonth: (previous: Boolean) -> Unit,
    moveToToday: () -> Unit
) {
    Card(modifier = modifier.wrapContentHeight()) {
        CalenderNavigation(
            currentDate = currentDate,
            viewMonth = viewMonth,
            moveToToday = moveToToday,
        )
        Divider(
            color = MaterialTheme.colorScheme.onPrimary,
            thickness = dimensionResource(id = R.dimen.two_dp)
        )
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Fixed(Utils.daysMap.size),
        ) {
            items(Utils.daysMap.keys.toList()) { items ->
                Text(
                    text = items.substring(0, 2),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.displaySmall
                )
            }
            items(calendar) { CircularCalenderCard(updateDate, it) }
        }
    }
}

@Composable
fun CalenderNavigation(
    viewMonth: (previous: Boolean) -> Unit,
    currentDate: LocalDate,
    moveToToday: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = MaterialTheme.spacing.large),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "${currentDate.month.name} ${currentDate.year}",
            style = MaterialTheme.typography.displayLarge
        )
        CircularButton(
            modifier = Modifier.size(dimensionResource(id = R.dimen.circular_text_button_size)),
            onClick = { moveToToday() }
        ) {
            Text(
                text = LocalDate.now().dayOfMonth.toString(),
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
        Row {
            CircularButton(
                modifier = Modifier.size(dimensionResource(id = R.dimen.circular_button_size)),
                onClick = { viewMonth(true) }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = stringResource(id = R.string.previous_month),
                )
            }
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.eight_dp)))
            CircularButton(
                modifier = Modifier.size(dimensionResource(id = R.dimen.circular_button_size)),
                onClick = { viewMonth(false) }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = stringResource(id = R.string.previous_month),
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewCalendarView() {
    val date = LocalDate.now().minusMonths(1)
    ExpenseAssistantTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            CalendarView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.large),
                calendar = Utils.createCalenderDays(
                    year = date.year,
                    month = date.monthValue,
                    date = date.dayOfMonth,
                ),
                updateDate = {},
                viewMonth = {},
                currentDate = LocalDate.now(),
                moveToToday = {}
            )
        }
    }
}