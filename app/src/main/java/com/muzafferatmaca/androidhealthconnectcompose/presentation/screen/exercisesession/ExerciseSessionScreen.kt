package com.muzafferatmaca.androidhealthconnectcompose.presentation.screen.exercisesession

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.muzafferatmaca.androidhealthconnectcompose.R
import com.muzafferatmaca.androidhealthconnectcompose.presentation.component.ExerciseSessionRow
import com.muzafferatmaca.androidhealthconnectcompose.presentation.theme.AndroidHealthConnectComposeTheme
import java.time.ZonedDateTime
import java.util.UUID

/**
 * Created by Muzaffer Atmaca on 4.06.2024 at 19:40
 */

@Composable
fun ExerciseSessionScreen(
    permissions: Set<String>,
    permissionsGranted: Boolean,
    sessionsList: List<ExerciseSessionRecord>,
    uiState: ExerciseUiState,
    onInsertClick: () -> Unit = {},
    onDetailsClick: (String) -> Unit = {},
    onError: (Throwable?) -> Unit = {},
    onPermissionsResult: () -> Unit = {},
    onPermissionsLaunch: (Set<String>) -> Unit = {},
) {

    // Remember the last error ID, such that it is possible to avoid re-launching the error
    // notification for the same error when the screen is recomposed, or configuration changes etc.
    val errorId = rememberSaveable { mutableStateOf(UUID.randomUUID()) }

    LaunchedEffect(uiState) {
        // If the initial data load has not taken place, attempt to load the data.
        if (uiState is ExerciseUiState.Uninitialized) {
            onPermissionsResult()
        }

        // The [ExerciseUiState] provides details of whether the last action was a
        // success or resulted in an error. Where an error occurred, for example in reading and
        // writing to Health Connect, the user is notified, and where the error is one that can be
        // recovered from, an attempt to do so is made.
        if (uiState is ExerciseUiState.Error && errorId.value != uiState.uuid) {
            onError(uiState.exception)
            errorId.value = uiState.uuid
        }
    }

    if (uiState != ExerciseUiState.Uninitialized) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!permissionsGranted) {
                item {
                    Button(
                        onClick = {
                            onPermissionsLaunch(permissions)
                        }
                    ) {
                        Text(text = stringResource(R.string.permissions_button_label))
                    }
                }
            } else {
                item {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(4.dp),
                        onClick = {
                            onInsertClick()
                        }
                    ) {
                        Text(stringResource(id = R.string.insert_exercise_session))
                    }
                }

                items(sessionsList) { session ->
                    ExerciseSessionRow(
                        ZonedDateTime.ofInstant(session.startTime, session.startZoneOffset),
                        ZonedDateTime.ofInstant(session.endTime, session.endZoneOffset),
                        session.metadata.id,
                        session.title ?: stringResource(R.string.no_title),
                        onDetailsClick = { uid ->
                            onDetailsClick(uid)
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ExerciseSessionScreenPreview() {
    AndroidHealthConnectComposeTheme {
        val runningStartTime = ZonedDateTime.now()
        val runningEndTime = runningStartTime.plusMinutes(30)
        val walkingStartTime = ZonedDateTime.now().minusMinutes(120)
        val walkingEndTime = walkingStartTime.plusMinutes(30)
        ExerciseSessionScreen(
            permissions = setOf(),
            permissionsGranted = true,
            sessionsList = listOf(
                ExerciseSessionRecord(
                    exerciseType = ExerciseSessionRecord.EXERCISE_TYPE_RUNNING,
                    title = "Running",
                    startTime = runningStartTime.toInstant(),
                    startZoneOffset = runningStartTime.offset,
                    endTime = runningEndTime.toInstant(),
                    endZoneOffset = runningEndTime.offset,
                    metadata = Metadata(UUID.randomUUID().toString())
                ),
                ExerciseSessionRecord(
                    exerciseType = ExerciseSessionRecord.EXERCISE_TYPE_WALKING,
                    title = "Walking",
                    startTime = walkingStartTime.toInstant(),
                    startZoneOffset = walkingStartTime.offset,
                    endTime = walkingEndTime.toInstant(),
                    endZoneOffset = walkingEndTime.offset,
                    metadata = Metadata(UUID.randomUUID().toString())
                )
            ),
            uiState = ExerciseUiState.Done
        )
    }
}