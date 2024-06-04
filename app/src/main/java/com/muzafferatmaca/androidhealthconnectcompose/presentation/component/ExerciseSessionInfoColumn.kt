package com.muzafferatmaca.androidhealthconnectcompose.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.muzafferatmaca.androidhealthconnectcompose.presentation.theme.AndroidHealthConnectComposeTheme
import java.time.ZonedDateTime
import java.util.UUID

/**
 * Created by Muzaffer Atmaca on 4.06.2024 at 17:40
 */
@Composable
fun ExerciseSessionInfoColumn(
    start: ZonedDateTime,
    end: ZonedDateTime,
    uid: String,
    name: String,
    onClick: (String) -> Unit = {},
) {
    Column(
        modifier = Modifier.clickable {
            onClick(uid)
        }
    ) {
        Text(
            color = MaterialTheme.colorScheme.primary,
            text = "${start.toLocalTime()} - ${end.toLocalTime()}",
            style = MaterialTheme.typography.labelLarge
        )
        Text(name)
        Text(uid)
    }
}

@Preview
@Composable
fun ExerciseSessionInfoColumnPreview() {
    AndroidHealthConnectComposeTheme {
        ExerciseSessionInfoColumn(
            ZonedDateTime.now().minusMinutes(30),
            ZonedDateTime.now(),
            UUID.randomUUID().toString(),
            "Running"
        )
    }
}
