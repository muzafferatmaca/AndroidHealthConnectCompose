package com.muzafferatmaca.androidhealthconnectcompose.presentation.screen.changes

import android.content.ContentValues
import android.os.RemoteException
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.health.connect.client.changes.Change
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.muzafferatmaca.androidhealthconnectcompose.data.HealthConnectManager
import com.muzafferatmaca.androidhealthconnectcompose.utils.ChangesMessage
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * Created by Muzaffer Atmaca on 4.06.2024 at 19:33
 */
class DifferentialChangesViewModel(private val healthConnectManager: HealthConnectManager) :
    ViewModel() {

    private val changesDataTypes = setOf(
        ExerciseSessionRecord::class,
        StepsRecord::class,
        TotalCaloriesBurnedRecord::class,
        HeartRateRecord::class,
        WeightRecord::class
    )

    val permissions = changesDataTypes.map { HealthPermission.getReadPermission(it) }.toSet()

    var permissionsGranted = mutableStateOf(false)
        private set

    var changesToken: MutableState<String?> = mutableStateOf(null)
        private set

    var changes = mutableStateListOf<Change>()
        private set

    var changesUiState: ChangesUiState by mutableStateOf(ChangesUiState.Uninitialized)
        private set

    val permissionsLauncher = healthConnectManager.requestPermissionsActivityContract()

    fun initialLoad() {
        viewModelScope.launch {
            permissionsGranted.value = healthConnectManager.hasAllPermissions(permissions)
            changesUiState = ChangesUiState.Done
        }
    }

    fun enableOrDisableChanges(enable: Boolean) {
        if (enable) {
            viewModelScope.launch {
                tryWithPermissionsCheck {
                    changesToken.value = healthConnectManager.getChangesToken()
                    Log.i(ContentValues.TAG, "Token: ${changesToken.value}")
                }
            }
        } else {
            changesToken.value = null
        }
    }

    fun getChanges() {
        viewModelScope.launch {
            tryWithPermissionsCheck {
                changesToken.value?.let { token ->
                    changes.clear()
                    healthConnectManager.getChanges(token).collect { message ->
                        when (message) {
                            is ChangesMessage.ChangeList -> {
                                changes.addAll(message.changes)
                            }
                            is ChangesMessage.NoMoreChanges -> {
                                changesToken.value = message.nextChangesToken
                                Log.i(ContentValues.TAG, "Updating changes token: ${changesToken.value}")
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun tryWithPermissionsCheck(block: suspend () -> Unit) {
        permissionsGranted.value = healthConnectManager.hasAllPermissions(permissions)
        changesUiState = try {
            if (permissionsGranted.value) {
                block()
            }
            ChangesUiState.Done
        } catch (remoteException: RemoteException) {
            ChangesUiState.Error(remoteException)
        } catch (securityException: SecurityException) {
            ChangesUiState.Error(securityException)
        } catch (ioException: IOException) {
            ChangesUiState.Error(ioException)
        } catch (illegalStateException: IllegalStateException) {
            ChangesUiState.Error(illegalStateException)
        }
    }


}

class DifferentialChangesViewModelFactory(
    private val healthConnectManager: HealthConnectManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DifferentialChangesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DifferentialChangesViewModel(
                healthConnectManager = healthConnectManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}