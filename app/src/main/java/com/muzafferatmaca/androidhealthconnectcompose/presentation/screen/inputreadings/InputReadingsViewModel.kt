package com.muzafferatmaca.androidhealthconnectcompose.presentation.screen.inputreadings

import android.os.RemoteException
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.units.Mass
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.muzafferatmaca.androidhealthconnectcompose.data.HealthConnectManager
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.Instant
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

/**
 * Created by Muzaffer Atmaca on 4.06.2024 at 19:58
 */
class InputReadingsViewModel(private val healthConnectManager: HealthConnectManager) :
    ViewModel() {
    val permissions = setOf(
        HealthPermission.getReadPermission(WeightRecord::class),
        HealthPermission.getWritePermission(WeightRecord::class),
    )
    var weeklyAvg: MutableState<Mass?> = mutableStateOf(Mass.kilograms(0.0))
        private set

    var permissionsGranted = mutableStateOf(false)
        private set

    var readingsList: MutableState<List<WeightRecord>> = mutableStateOf(listOf())
        private set

    var readingUiState: ReadingUiState by mutableStateOf(ReadingUiState.Uninitialized)
        private set

    val permissionsLauncher = healthConnectManager.requestPermissionsActivityContract()

    fun initialLoad() {
        viewModelScope.launch {
            tryWithPermissionsCheck {
                readWeightInputs()
            }
        }
    }

    fun inputReadings(inputValue: Double) {
        viewModelScope.launch {
            tryWithPermissionsCheck {
                healthConnectManager.writeWeightInput(inputValue)
                readWeightInputs()
            }
        }
    }

    private suspend fun readWeightInputs() {
        val startOfDay = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS)
        val now = Instant.now()
        val endofWeek = startOfDay.toInstant().plus(7, ChronoUnit.DAYS)
        readingsList.value = healthConnectManager.readWeightInputs(startOfDay.toInstant(), now)
        weeklyAvg.value =
            healthConnectManager.computeWeeklyAverage(startOfDay.toInstant(), endofWeek)
    }

    private suspend fun tryWithPermissionsCheck(block: suspend () -> Unit) {
        permissionsGranted.value = healthConnectManager.hasAllPermissions(permissions)
        readingUiState = try {
            if (permissionsGranted.value) {
                block()
            }
            ReadingUiState.Done
        } catch (remoteException: RemoteException) {
            ReadingUiState.Error(remoteException)
        } catch (securityException: SecurityException) {
            ReadingUiState.Error(securityException)
        } catch (ioException: IOException) {
            ReadingUiState.Error(ioException)
        } catch (illegalStateException: IllegalStateException) {
            ReadingUiState.Error(illegalStateException)
        }
    }


}

class InputReadingsViewModelFactory(
    private val healthConnectManager: HealthConnectManager,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InputReadingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InputReadingsViewModel(
                healthConnectManager = healthConnectManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
