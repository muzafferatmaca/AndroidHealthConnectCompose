package com.muzafferatmaca.androidhealthconnectcompose.presentation.screen.actvitiy

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.muzafferatmaca.androidhealthconnectcompose.R
import com.muzafferatmaca.androidhealthconnectcompose.data.HealthConnectManager
import com.muzafferatmaca.androidhealthconnectcompose.presentation.navigation.Drawer
import com.muzafferatmaca.androidhealthconnectcompose.presentation.navigation.HealthConnectNavigation
import com.muzafferatmaca.androidhealthconnectcompose.presentation.navigation.Screen
import com.muzafferatmaca.androidhealthconnectcompose.presentation.theme.AndroidHealthConnectComposeTheme
import com.muzafferatmaca.androidhealthconnectcompose.utils.HealthConnectAvailability
import kotlinx.coroutines.launch

/**
 * Created by Muzaffer Atmaca on 4.06.2024 at 20:02
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthConnectApp(healthConnectManager: HealthConnectManager) {
    AndroidHealthConnectComposeTheme {
        val snackbarHostState = remember { SnackbarHostState() }
        val navController = rememberNavController()
        val scope = rememberCoroutineScope()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val availability by healthConnectManager.availability

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                if (availability == HealthConnectAvailability.INSTALLED) {
                    Surface(
                        color = MaterialTheme.colorScheme.surface,

                    ) {
                        Drawer(
                            scope = scope,
                            drawerState = drawerState,
                            navController = navController
                        )
                    }
                }
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            val titleId = when (currentRoute) {
                                Screen.ExerciseSessions.toString() -> Screen.ExerciseSessions.toString()
                                Screen.InputReadings.toString() -> Screen.InputReadings.toString()
                                Screen.DifferentialChanges.toString() -> Screen.DifferentialChanges.toString()
                                else -> R.string.app_name
                            }
                            Text(text = titleId.toString())
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    if (availability == HealthConnectAvailability.INSTALLED) {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Menu,
                                    contentDescription = stringResource(id = R.string.menu)
                                )
                            }
                        }
                    )
                },
                snackbarHost = {
                    SnackbarHost(snackbarHostState)
                }
            ) { _ ->
                HealthConnectNavigation(
                    healthConnectManager = healthConnectManager,
                    navController = navController,
                    snackbarState = snackbarHostState,
                )
            }
        }
    }
}