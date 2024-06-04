package com.muzafferatmaca.androidhealthconnectcompose.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.muzafferatmaca.androidhealthconnectcompose.R
import com.muzafferatmaca.androidhealthconnectcompose.presentation.theme.AndroidHealthConnectComposeTheme

/**
 * Created by Muzaffer Atmaca on 4.06.2024 at 17:40
 */
@Composable
fun ExerciseSessionDetailsMinMaxAvg(
    minimum: String?,
    maximum: String?,
    average: String?,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .weight(1f),
            text = stringResource(
                R.string.label_and_value,
                stringResource(R.string.min_label),
                minimum ?: "N/A"
            ),
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .weight(1f),
            text = stringResource(
                R.string.label_and_value,
                stringResource(R.string.max_label),
                maximum ?: "N/A"
            ),
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .weight(1f),
            text = stringResource(
                R.string.label_and_value,
                stringResource(R.string.avg_label),
                average ?: "N/A"
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun ExerciseSessionDetailsMinMaxAvgPreview() {
    AndroidHealthConnectComposeTheme {
        ExerciseSessionDetailsMinMaxAvg(minimum = "10", maximum = "100", average = "55")
    }
}