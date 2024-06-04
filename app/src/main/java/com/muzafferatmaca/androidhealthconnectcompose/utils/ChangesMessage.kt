package com.muzafferatmaca.androidhealthconnectcompose.util

import androidx.health.connect.client.changes.Change

/**
 * Created by Muzaffer Atmaca on 4.06.2024 at 19:06
 */
sealed class ChangesMessage {
    data class NoMoreChanges(val nextChangesToken: String) : ChangesMessage()
    data class ChangeList(val changes: List<Change>) : ChangesMessage()
}