package com.muzafferatmaca.androidhealthconnectcompose.presentation.screen.changes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.changes.Change
import com.muzafferatmaca.androidhealthconnectcompose.R
import com.muzafferatmaca.androidhealthconnectcompose.presentation.component.FormattedChange
import java.util.UUID

/**
 * Created by Muzaffer Atmaca on 4.06.2024 at 19:36
 */
@Composable
fun DifferentialChangesScreen(
    permissions: Set<String>,
    permissionsGranted: Boolean,
    changesEnabled: Boolean,
    onChangesEnable: (Boolean) -> Unit,
    onGetChanges: () -> Unit,
    changes: List<Change>,
    changesToken: String?,
    uiState: ChangesUiState,
    onError: (Throwable?) -> Unit = {},
    onPermissionsResult: () -> Unit = {},
    onPermissionsLaunch: (Set<String>) -> Unit = {}
) {

    // Remember the last error ID, such that it is possible to avoid re-launching the error
    // notification for the same error when the screen is recomposed, or configuration changes etc.
    val errorId = rememberSaveable { mutableStateOf(UUID.randomUUID()) }

    LaunchedEffect(uiState) {
        // If the initial data load has not taken place, attempt to load the data.
        if (uiState is ChangesUiState.Uninitialized) {
            onPermissionsResult()
        }

        // The [DifferentialChangesViewModel.UiState] provides details of whether the last action
        // was a success or resulted in an error. Where an error occurred, for example in reading
        // and writing to Health Connect, the user is notified, and where the error is one that can
        // be recovered from, an attempt to do so is made.
        if (uiState is ChangesUiState.Error && errorId.value != uiState.uuid) {
            onError(uiState.exception)
            errorId.value = uiState.uuid
        }
    }

    if (uiState != ChangesUiState.Uninitialized) {
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
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = stringResource(R.string.differential_changes_title_text),
                        textAlign = TextAlign.Justify
                    )
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = stringResource(R.string.differential_changes_continuation_text),
                        textAlign = TextAlign.Justify
                    )
                }
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(stringResource(R.string.differential_changes_switch_text))
                        Switch(
                            checked = changesEnabled,
                            onCheckedChange = onChangesEnable
                        )
                    }
                }

                item {
                    val token = changesToken ?: stringResource(id = R.string.not_available_abbrev)
                    Text(stringResource(id = R.string.differential_changes_current_token, token))
                }

                item {
                    Button(
                        modifier = Modifier.padding(8.dp),
                        enabled = changesEnabled,
                        onClick = onGetChanges
                    ) {
                        Text(stringResource(R.string.differential_changes_button_text))
                    }
                }

                items(changes) { changeItem ->
                    FormattedChange(changeItem)
                }
                if (changes.isEmpty()) {
                    item {
                        Text(stringResource(R.string.differential_changes_empty))
                    }
                }
            }
        }
    }
}