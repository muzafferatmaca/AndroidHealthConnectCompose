package com.muzafferatmaca.androidhealthconnectcompose.presentation.screen

import android.app.Application
import com.muzafferatmaca.androidhealthconnectcompose.data.HealthConnectManager

/**
 * Created by Muzaffer Atmaca on 4.06.2024 at 20:00
 */
class App : Application() {
    val healthConnectManager by lazy {
        HealthConnectManager(this)
    }
}
