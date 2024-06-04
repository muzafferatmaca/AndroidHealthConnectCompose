package com.muzafferatmaca.androidhealthconnectcompose.presentation.screen.welcomescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.muzafferatmaca.androidhealthconnectcompose.R
import com.muzafferatmaca.androidhealthconnectcompose.presentation.component.InstalledMessage
import com.muzafferatmaca.androidhealthconnectcompose.presentation.component.NotInstalledMessage
import com.muzafferatmaca.androidhealthconnectcompose.presentation.component.NotSupportedMessage
import com.muzafferatmaca.androidhealthconnectcompose.presentation.theme.AndroidHealthConnectComposeTheme
import com.muzafferatmaca.androidhealthconnectcompose.utils.HealthConnectAvailability

/**
 * Created by Muzaffer Atmaca on 4.06.2024 at 16:09
 */
@Composable
fun WelcomeScreen(
    healthConnectAvailability: HealthConnectAvailability,
    onResumeAvailabilityCheck: () -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {

    val currentOnAvailabilityCheck by rememberUpdatedState(newValue = onResumeAvailabilityCheck)

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                currentOnAvailabilityCheck()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(0.5f),
            painter = painterResource(id = R.drawable.ic_health_connect_logo),
            contentDescription = stringResource(id = R.string.health_connect_logo)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(id = R.string.welcome_message),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(32.dp))
        when(healthConnectAvailability){
            HealthConnectAvailability.INSTALLED -> InstalledMessage()
            HealthConnectAvailability.NOT_INSTALLED -> NotInstalledMessage()
            HealthConnectAvailability.NOT_SUPPORTED -> NotSupportedMessage()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InstalledMessagePreview() {
    AndroidHealthConnectComposeTheme {
        WelcomeScreen(
            healthConnectAvailability = HealthConnectAvailability.INSTALLED,
            onResumeAvailabilityCheck = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotInstalledMessagePreview() {
    AndroidHealthConnectComposeTheme {
        WelcomeScreen(
            healthConnectAvailability = HealthConnectAvailability.NOT_INSTALLED,
            onResumeAvailabilityCheck = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotSupportedMessagePreview() {
    AndroidHealthConnectComposeTheme {
        WelcomeScreen(
            healthConnectAvailability = HealthConnectAvailability.NOT_SUPPORTED,
            onResumeAvailabilityCheck = {}
        )
    }
}
