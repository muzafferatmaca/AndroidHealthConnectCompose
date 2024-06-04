package com.muzafferatmaca.androidhealthconnectcompose.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.muzafferatmaca.androidhealthconnectcompose.util.HealthConnectAvailability

/**
 * Created by Muzaffer Atmaca on 4.06.2024 at 16:09
 */
@Composable
fun WelcomeScreen(
    healthConnectAvailability: HealthConnectAvailability,
    onResumeAvailabilityCheck : () -> Unit,
    lifecycleOwner : LifecycleOwner = LocalLifecycleOwner.current
){

    val currentOnAvailabilityCheck by rememberUpdatedState(newValue =  onResumeAvailabilityCheck)


}