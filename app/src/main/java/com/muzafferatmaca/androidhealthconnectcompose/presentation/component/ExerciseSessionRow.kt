package com.muzafferatmaca.androidhealthconnectcompose.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muzafferatmaca.androidhealthconnectcompose.presentation.theme.AndroidHealthConnectComposeTheme
import java.time.ZonedDateTime
import java.util.UUID

/**
 * Created by Muzaffer Atmaca on 4.06.2024 at 17:44
 */
@Composable
fun ExerciseSessionRow(
    start: ZonedDateTime,
    end: ZonedDateTime,
    uid: String,
    name: String,
    onDetailsClick: (String) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ExerciseSessionInfoColumn(
            start = start,
            end = end,
            uid = uid,
            name = name,
            onClick = onDetailsClick
        )
    }
}

@Preview
@Composable
fun ExerciseSessionRowPreview() {
    AndroidHealthConnectComposeTheme {
        ExerciseSessionRow(
            ZonedDateTime.now().minusMinutes(30),
            ZonedDateTime.now(),
            UUID.randomUUID().toString(),
            "Running"
        )
    }
}
