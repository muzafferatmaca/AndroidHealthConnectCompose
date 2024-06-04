package com.muzafferatmaca.androidhealthconnectcompose.presentation.screen.exercisesessiondetail

import android.os.RemoteException
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.muzafferatmaca.androidhealthconnectcompose.data.ExerciseSessionData
import com.muzafferatmaca.androidhealthconnectcompose.data.HealthConnectManager
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.UUID

/**
 * Created by Muzaffer Atmaca on 4.06.2024 at 19:45
 */
class ExerciseSessionDetailViewModel(
    private val uid: String,
    private val healthConnectManager: HealthConnectManager,
) : ViewModel() {
    val permissions = setOf(
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class),
        HealthPermission.getReadPermission(HeartRateRecord::class)
    )

    var permissionsGranted = mutableStateOf(false)
        private set

    var sessionMetrics: MutableState<ExerciseSessionData> = mutableStateOf(ExerciseSessionData(uid))
        private set

    var uiState: ExerciseDetailUiState by mutableStateOf(ExerciseDetailUiState.Uninitialized)
        private set

    val permissionsLauncher = healthConnectManager.requestPermissionsActivityContract()

    fun initialLoad() {
        readAssociatedSessionData()
    }

    private fun readAssociatedSessionData() {
        viewModelScope.launch {
            tryWithPermissionsCheck {
                sessionMetrics.value = healthConnectManager.readAssociatedSessionData(uid)
            }
        }
    }

    /**
     * Provides permission check and error handling for Health Connect suspend function calls.
     *
     * Permissions are checked prior to execution of [block], and if all permissions aren't granted
     * the [block] won't be executed, and [permissionsGranted] will be set to false, which will
     * result in the UI showing the permissions button.
     *
     * Where an error is caught, of the type Health Connect is known to throw, [uiState] is set to
     * [ExerciseDetailUiState.Error], which results in the snackbar being used to show the error message.
     */
    private suspend fun tryWithPermissionsCheck(block: suspend () -> Unit) {
        permissionsGranted.value = healthConnectManager.hasAllPermissions(permissions)
        uiState = try {
            if (permissionsGranted.value) {
                block()
            }
            ExerciseDetailUiState.Done
        } catch (remoteException: RemoteException) {
            ExerciseDetailUiState.Error(remoteException)
        } catch (securityException: SecurityException) {
            ExerciseDetailUiState.Error(securityException)
        } catch (ioException: IOException) {
            ExerciseDetailUiState.Error(ioException)
        } catch (illegalStateException: IllegalStateException) {
            ExerciseDetailUiState.Error(illegalStateException)
        }
    }


}

class ExerciseSessionDetailViewModelFactory(
    private val uid: String,
    private val healthConnectManager: HealthConnectManager,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExerciseSessionDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExerciseSessionDetailViewModel(
                uid = uid,
                healthConnectManager = healthConnectManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}