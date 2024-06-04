package com.muzafferatmaca.androidhealthconnectcompose.presentation.navigation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.muzafferatmaca.androidhealthconnectcompose.data.HealthConnectManager
import com.muzafferatmaca.androidhealthconnectcompose.presentation.screen.changes.DifferentialChangesScreen
import com.muzafferatmaca.androidhealthconnectcompose.presentation.screen.changes.DifferentialChangesViewModel
import com.muzafferatmaca.androidhealthconnectcompose.presentation.screen.changes.DifferentialChangesViewModelFactory
import com.muzafferatmaca.androidhealthconnectcompose.presentation.screen.exercisesession.ExerciseSessionScreen
import com.muzafferatmaca.androidhealthconnectcompose.presentation.screen.exercisesession.ExerciseSessionViewModel
import com.muzafferatmaca.androidhealthconnectcompose.presentation.screen.exercisesession.ExerciseSessionViewModelFactory
import com.muzafferatmaca.androidhealthconnectcompose.presentation.screen.exercisesessiondetail.ExerciseSessionDetailScreen
import com.muzafferatmaca.androidhealthconnectcompose.presentation.screen.exercisesessiondetail.ExerciseSessionDetailViewModel
import com.muzafferatmaca.androidhealthconnectcompose.presentation.screen.exercisesessiondetail.ExerciseSessionDetailViewModelFactory
import com.muzafferatmaca.androidhealthconnectcompose.presentation.screen.inputreadings.InputReadingsScreen
import com.muzafferatmaca.androidhealthconnectcompose.presentation.screen.inputreadings.InputReadingsViewModel
import com.muzafferatmaca.androidhealthconnectcompose.presentation.screen.inputreadings.InputReadingsViewModelFactory
import com.muzafferatmaca.androidhealthconnectcompose.presentation.screen.privacypolicy.PrivacyPolicyScreen
import com.muzafferatmaca.androidhealthconnectcompose.presentation.screen.welcomescreen.WelcomeScreen
import com.muzafferatmaca.androidhealthconnectcompose.utils.HealthConnectAvailability
import com.muzafferatmaca.androidhealthconnectcompose.utils.showExceptionSnackBar
import kotlinx.coroutines.CoroutineScope

/**
 * Created by Muzaffer Atmaca on 4.06.2024 at 20:08
 */
@Composable
fun HealthConnectNavigation(
    navController: NavHostController,
    healthConnectManager: HealthConnectManager,
    snackbarState: SnackbarHostState,
) {
    val scope = rememberCoroutineScope()
    NavHost(navController = navController, startDestination = Screen.WelcomeScreen.route) {
        val availability by healthConnectManager.availability
        welcomeScreen(availability,healthConnectManager)
        exerciseSessions(healthConnectManager,navController,snackbarState,scope)
        exerciseSessionDetail(healthConnectManager,snackbarState,scope,)
        inputReadings(healthConnectManager,snackbarState,scope)
        differentialChanges(healthConnectManager,snackbarState,scope)
        privacyPolicy()
    }
}

fun NavGraphBuilder.welcomeScreen(
    availability: HealthConnectAvailability,
    healthConnectManager: HealthConnectManager
) {
    composable(Screen.WelcomeScreen.route) {
        WelcomeScreen(
            healthConnectAvailability = availability,
            onResumeAvailabilityCheck = {
                healthConnectManager.checkAvailability()
            }
        )
    }
}

fun NavGraphBuilder.exerciseSessions(
    healthConnectManager: HealthConnectManager,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    composable(Screen.ExerciseSessions.route) {
        val viewModel: ExerciseSessionViewModel = viewModel(
            factory = ExerciseSessionViewModelFactory(
                healthConnectManager = healthConnectManager
            )
        )
        val permissionsGranted by viewModel.permissionsGranted
        val sessionsList by viewModel.sessionsList
        val permissions = viewModel.permissions
        val onPermissionsResult = { viewModel.initialLoad() }
        val permissionsLauncher =
            rememberLauncherForActivityResult(viewModel.permissionsLauncher) {
                onPermissionsResult()
            }
        ExerciseSessionScreen(
            permissionsGranted = permissionsGranted,
            permissions = permissions,
            sessionsList = sessionsList,
            uiState = viewModel.exerciseUiState,
            onInsertClick = {
                viewModel.insertExerciseSession()
            },
            onDetailsClick = { uid ->
                navController.navigate(Screen.ExerciseSessionDetail.passArgs(uid))
            },
            onError = { exception ->
                showExceptionSnackBar(snackbarHostState, scope, exception)
            },
            onPermissionsResult = {
                viewModel.initialLoad()
            },
            onPermissionsLaunch = { values ->
                permissionsLauncher.launch(values)
            }
        )
    }
}

fun NavGraphBuilder.exerciseSessionDetail(
    healthConnectManager: HealthConnectManager,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
) {
    composable(Screen.ExerciseSessionDetail.route) {
        val uid = it.arguments?.getString(UID_NAV_ARGUMENT)!!
        val viewModel: ExerciseSessionDetailViewModel = viewModel(
            factory = ExerciseSessionDetailViewModelFactory(
                uid = uid,
                healthConnectManager = healthConnectManager
            )
        )
        val permissionsGranted by viewModel.permissionsGranted
        val sessionMetrics by viewModel.sessionMetrics
        val permissions = viewModel.permissions
        val onPermissionsResult = { viewModel.initialLoad() }
        val permissionsLauncher =
            rememberLauncherForActivityResult(viewModel.permissionsLauncher) {
                onPermissionsResult()
            }
        ExerciseSessionDetailScreen(
            permissions = permissions,
            permissionsGranted = permissionsGranted,
            sessionMetrics = sessionMetrics,
            uiState = viewModel.uiState,
            onError = { exception ->
                showExceptionSnackBar(snackbarHostState, scope, exception)
            },
            onPermissionsResult = {
                viewModel.initialLoad()
            },
            onPermissionsLaunch = { values ->
                permissionsLauncher.launch(values)
            }
        )
    }
}

fun NavGraphBuilder.inputReadings(
    healthConnectManager: HealthConnectManager,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    composable(Screen.InputReadings.route) {
        val viewModel: InputReadingsViewModel = viewModel(
            factory = InputReadingsViewModelFactory(
                healthConnectManager = healthConnectManager
            )
        )
        val permissionsGranted by viewModel.permissionsGranted
        val readingsList by viewModel.readingsList
        val permissions = viewModel.permissions
        val weeklyAvg by viewModel.weeklyAvg
        val onPermissionsResult = { viewModel.initialLoad() }
        val permissionsLauncher =
            rememberLauncherForActivityResult(viewModel.permissionsLauncher) {
                onPermissionsResult()
            }
        InputReadingsScreen(
            permissionsGranted = permissionsGranted,
            permissions = permissions,

            uiState = viewModel.readingUiState,
            onInsertClick = { weightInput ->
                viewModel.inputReadings(weightInput)
            },
            weeklyAvg = weeklyAvg,
            readingsList = readingsList,
            onError = { exception ->
                showExceptionSnackBar(snackbarHostState, scope, exception)
            },
            onPermissionsResult = {
                viewModel.initialLoad()
            },
            onPermissionsLaunch = { values ->
                permissionsLauncher.launch(values)
            }
        )
    }
}

fun NavGraphBuilder.differentialChanges(
    healthConnectManager: HealthConnectManager,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    composable(Screen.DifferentialChanges.route) {
        val viewModel: DifferentialChangesViewModel = viewModel(
            factory = DifferentialChangesViewModelFactory(
                healthConnectManager = healthConnectManager
            )
        )
        val changesToken by viewModel.changesToken
        val permissionsGranted by viewModel.permissionsGranted
        val permissions = viewModel.permissions
        val onPermissionsResult = {viewModel.initialLoad()}
        val permissionsLauncher =
            rememberLauncherForActivityResult(viewModel.permissionsLauncher) {
                onPermissionsResult()}
        DifferentialChangesScreen(
            permissionsGranted = permissionsGranted,
            permissions = permissions,
            changesEnabled = changesToken != null,
            onChangesEnable = { enabled ->
                viewModel.enableOrDisableChanges(enabled)
            },
            changes = viewModel.changes,
            changesToken = changesToken,
            onGetChanges = {
                viewModel.getChanges()
            },
            uiState = viewModel.changesUiState,
            onError = { exception ->
                showExceptionSnackBar(snackbarHostState, scope, exception)
            },
            onPermissionsResult = {
                viewModel.initialLoad()
            },
            onPermissionsLaunch = { values ->
                permissionsLauncher.launch(values)}
        )
    }
}

fun NavGraphBuilder.privacyPolicy() {
    composable(
        route = Screen.PrivacyPolicy.route,
        deepLinks = listOf(
            navDeepLink {
                action = "androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE"
            }
        )
    ) {
        PrivacyPolicyScreen()
    }
}

