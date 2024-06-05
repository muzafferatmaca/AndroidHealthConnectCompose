package com.muzafferatmaca.androidhealthconnectcompose.presentation.navigation

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.HealthConnectClient
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.muzafferatmaca.androidhealthconnectcompose.R
import com.muzafferatmaca.androidhealthconnectcompose.presentation.theme.AndroidHealthConnectComposeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by Muzaffer Atmaca on 4.06.2024 at 19:09
 */
@Composable
fun Drawer(
    scope: CoroutineScope,
    drawerState: DrawerState,
    navController: NavController,
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination
    val activity = LocalContext.current

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .width(96.dp)
                    .clickable {
                        navController.navigate(Screen.WelcomeScreen) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                        scope.launch {
                            drawerState.close()
                        }
                    },
                painter = painterResource(id = R.drawable.ic_health_connect_logo),
                contentDescription = stringResource(id = R.string.health_connect_logo)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.app_name)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Screen.allScreens.filter { it.hasMenuItem }.forEach {item ->
            DrawerItem(
                item = item,
                selected = item == currentRoute,
                onItemClick = {
                    navController.navigate(item) {
                        // See: https://developer.android.com/jetpack/compose/navigation#nav-to-composable
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(1.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = {
                        val settingsIntent = Intent()
                        settingsIntent.action = HealthConnectClient.ACTION_HEALTH_CONNECT_SETTINGS
                        activity.startActivity(settingsIntent)
                    }
                )
                .height(48.dp)
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.settings),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DrawerPreview() {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Open)
    val navController = rememberNavController()
    AndroidHealthConnectComposeTheme {
        Drawer(
            scope = scope,
            drawerState = drawerState,
            navController = navController
        )
    }
}