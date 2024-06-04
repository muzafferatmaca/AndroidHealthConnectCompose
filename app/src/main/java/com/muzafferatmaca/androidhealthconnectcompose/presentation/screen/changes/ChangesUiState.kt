package com.muzafferatmaca.androidhealthconnectcompose.presentation.screen.changes

import java.util.UUID

/**
 * Created by Muzaffer Atmaca on 4.06.2024 at 19:35
 */
sealed class ChangesUiState {
    data object Uninitialized : ChangesUiState()
    data object Done : ChangesUiState()

    // A random UUID is used in each Error object to allow errors to be uniquely identified,
    // and recomposition won't result in multiple snackbars.
    data class Error(val exception: Throwable, val uuid: UUID = UUID.randomUUID()) : ChangesUiState()
}