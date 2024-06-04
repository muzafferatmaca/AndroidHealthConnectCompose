package com.muzafferatmaca.androidhealthconnectcompose.utils

import android.annotation.SuppressLint
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

/**
 * Created by Muzaffer Atmaca on 4.06.2024 at 17:36
 */
fun dateTimeWithOffsetOrDefault(time: Instant, offset: ZoneOffset?): ZonedDateTime =
    if (offset != null) {
        ZonedDateTime.ofInstant(time, offset)
    } else {
        ZonedDateTime.ofInstant(time, ZoneId.systemDefault())
    }

@SuppressLint("DefaultLocale")
fun Duration.formatTime() = String.format(
    "%02d:%02d:%02d",
    this.toHours() % 24,
    this.toMinutes() % 60,
    this.seconds % 60
)

@SuppressLint("DefaultLocale")
fun Duration.formatHoursMinutes() = String.format(
    "%01dh%02dm",
    this.toHours() % 24,
    this.toMinutes() % 60
)