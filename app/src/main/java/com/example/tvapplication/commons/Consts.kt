package com.example.tvapplication.commons

import android.app.PendingIntent

const val DAYS_SELECTED = "DAYS_SELECTED"
const val TITLE = "TITLE"
const val HOUR = "HOUR"
const val MINUTE = "MINUTE"
const val HOLIDAY_CODE = 1368
object GlobalProperties {

    const val TIME_FORMAT = "%02d:%02d:%02d"

    const val PendingIntentFlags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
}