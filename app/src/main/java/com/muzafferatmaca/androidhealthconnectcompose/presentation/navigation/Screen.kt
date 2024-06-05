package com.muzafferatmaca.androidhealthconnectcompose.presentation.navigation

import androidx.compose.animation.fadeIn
import com.muzafferatmaca.androidhealthconnectcompose.R
import kotlinx.serialization.Serializable

/**
 * Created by Muzaffer Atmaca on 4.06.2024 at 17:10
 */
const val UID_NAV_ARGUMENT = "uid"

@Serializable
sealed class Screen(val hasMenuItem: Boolean = true) {
    @Serializable
    data object WelcomeScreen : Screen(hasMenuItem = false)

    @Serializable
    data object ExerciseSessions : Screen()

    @Serializable
    data class ExerciseSessionDetail(val id: String) : Screen(hasMenuItem = false)

    @Serializable
    data object InputReadings : Screen()

    @Serializable
    data object DifferentialChanges : Screen()

    @Serializable
    data object PrivacyPolicy : Screen(hasMenuItem = false)

    companion object {
        val allScreens = listOf(
            ExerciseSessions,
            InputReadings,
            DifferentialChanges,
        )
    }

}