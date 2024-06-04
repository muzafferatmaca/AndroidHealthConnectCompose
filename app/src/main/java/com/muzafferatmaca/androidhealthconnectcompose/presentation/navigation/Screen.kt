package com.muzafferatmaca.androidhealthconnectcompose.presentation.navigation

import androidx.compose.animation.fadeIn
import com.muzafferatmaca.androidhealthconnectcompose.R

/**
 * Created by Muzaffer Atmaca on 4.06.2024 at 17:10
 */
const val UID_NAV_ARGUMENT = "uid"

sealed class Screen(val route: String, val titleId: Int, val hasMenuItem: Boolean = true) {
    data object WelcomeScreen : Screen(route = "welcome_screen", titleId = R.string.welcome_screen, hasMenuItem = false)
    data object ExerciseSessions : Screen(route = "exercise_sessions", titleId = R.string.exercise_sessions)
    data object ExerciseSessionDetail : Screen(
        route = "exercise_session_detail?$UID_NAV_ARGUMENT={$UID_NAV_ARGUMENT}",
        titleId = R.string.exercise_session_detail,
        hasMenuItem = false
    ) {
        fun passArgs(args: String) = "exercise_session_detail?$UID_NAV_ARGUMENT = $args"
    }
    data object InputReadings : Screen(route = "input_readings", titleId = R.string.input_readings)
    data object DifferentialChanges : Screen(route = "differential_changes", titleId = R.string.differential_changes)
    data object PrivacyPolicy : Screen(route = "privacy_policy", titleId = R.string.privacy_policy, hasMenuItem = false)

    companion object {
        val allScreens = listOf(
            WelcomeScreen,
            ExerciseSessions,
            ExerciseSessionDetail,
            InputReadings,
            DifferentialChanges,
            PrivacyPolicy
        )
    }

}