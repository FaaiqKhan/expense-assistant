package com.practice.expenseAssistant.ui.categoryScreen

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme
import com.practice.expenseAssistant.utils.IncomeCategoriesIcon
import com.practice.expenseAssistant.utils.IncomeType

@Composable
fun IncomeCategories(modifier: Modifier = Modifier, onSelect: (incomeType: String) -> Unit) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
    ) {
        items(IncomeType.values()) {
            Row(modifier = Modifier.clickable { onSelect(it.value) }) {
                Icon(
                    imageVector = IncomeCategoriesIcon.icons.getValue(it.value),
                    contentDescription = it.name
                )
                Text(
                    text = it.value,
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen.card_padding)),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewIncomeCategories() {
    ExpenseAssistantTheme {
        IncomeCategories(modifier = Modifier.fillMaxWidth(), onSelect = {})
    }
}