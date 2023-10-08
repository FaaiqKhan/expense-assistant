package com.practice.expenseAssistant.ui.aboutScreen

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.practice.expenseAssistant.R
import com.practice.expenseAssistant.ui.theme.ExpenseAssistantTheme

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = stringResource(R.string.my_name), style = MaterialTheme.typography.displayLarge)
        Row(
            modifier = Modifier.padding(top = dimensionResource(id = R.dimen.eight_dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Mail,
                contentDescription = stringResource(R.string.my_email_address),
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.eight_dp)))
            Text(
                text = stringResource(R.string.faiqalikhan49_gmail_com),
                style = MaterialTheme.typography.displaySmall,
            )
        }
        Text(
            text = stringResource(id = R.string.about_me_header),
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.eight_dp))
        )
        Text(
            text = stringResource(id = R.string.about_me_body),
            style = MaterialTheme.typography.displaySmall,
        )
        // About Application
        Text(
            text = stringResource(id = R.string.about_application),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.eight_dp))
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun AboutScreenPreview() {
    ExpenseAssistantTheme {
        AboutScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = dimensionResource(id = R.dimen.screen_content_padding))
        )
    }
}