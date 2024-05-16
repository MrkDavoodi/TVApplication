package com.example.tvapplication.commons

import android.util.DisplayMetrics
import com.example.tvapplication.MainActivity
import java.util.Calendar

private fun calculateSpanCount(activity: MainActivity): Int {
    val displayMetrics = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
    val screenWidth = displayMetrics.widthPixels / displayMetrics.density

    return when {
        screenWidth >= 960 -> 7 // Android TV
        screenWidth >= 600 -> 5  // Tablet
        else -> 3  // Mobile
    }
}
fun getDayOfWeek(day: String): Int {
    return when (day) {
        "mon"->Calendar.MONDAY
        "tue"->Calendar.TUESDAY
        "eed"->Calendar.WEDNESDAY
        "thu"->Calendar.THURSDAY
        "fri"->Calendar.FRIDAY
        "sat"->Calendar.SATURDAY
        "sun"->Calendar.SUNDAY
        else -> 0
    }
}