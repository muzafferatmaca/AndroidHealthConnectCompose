package com.muzafferatmaca.androidhealthconnectcompose.presentation.component

import androidx.annotation.StringRes
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.muzafferatmaca.androidhealthconnectcompose.R
import com.muzafferatmaca.androidhealthconnectcompose.presentation.theme.AndroidHealthConnectComposeTheme

/**
 * Created by Muzaffer Atmaca on 4.06.2024 at 17:37
 */
fun LazyListScope.sessionDetailsItem(
    @StringRes labelId: Int,
    content: @Composable () -> Unit,
) {
    item {
        Text(
            text = stringResource(id = labelId),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}

@Preview
@Composable
fun SessionDetailsItemPreview() {
    AndroidHealthConnectComposeTheme {
        LazyColumn {
            sessionDetailsItem(R.string.total_steps) {
                Text(text = "12345")
            }
        }
    }
}