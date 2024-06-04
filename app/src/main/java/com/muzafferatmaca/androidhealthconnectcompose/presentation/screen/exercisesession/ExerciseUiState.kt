package com.muzafferatmaca.androidhealthconnectcompose.presentation.screen.exercisesession

import java.util.UUID

/**
 * Created by Muzaffer Atmaca on 4.06.2024 at 19:35
 */
sealed class ExerciseUiState {
    data object Uninitialized : ExerciseUiState()
    data object Done : ExerciseUiState()

    // A random UUID is used in each Error object to allow errors to be uniquely identified,
    // and recomposition won't result in multiple snackbars.
    data class Error(val exception: Throwable, val uuid: UUID = UUID.randomUUID()) : ExerciseUiState()
}