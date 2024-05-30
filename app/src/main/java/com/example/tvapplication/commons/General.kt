package com.example.tvapplication.commons

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import android.os.PowerManager
import android.util.DisplayMetrics
import android.util.Log
import androidx.media3.datasource.DataSpec.Flags
import com.example.tvapplication.MainActivity
import com.example.tvapplication.admin.MyDeviceAdminReceiver
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
        "mon" -> Calendar.MONDAY
        "tue" -> Calendar.TUESDAY
        "wed" -> Calendar.WEDNESDAY
        "thu" -> Calendar.THURSDAY
        "fri" -> Calendar.FRIDAY
        "sat" -> Calendar.SATURDAY
        "sun" -> Calendar.SUNDAY
        else -> 0
    }
}


fun putDeviceToSleep(context: Context) {
    val devicePolicyManager = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    val componentName = ComponentName(context, MyDeviceAdminReceiver::class.java)
    if (!devicePolicyManager.isAdminActive(componentName)) {
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "دریافت مجوز ")
        intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
    if(devicePolicyManager.isAdminActive(componentName)) {
        devicePolicyManager.lockNow()
    }
}
fun sendDeviceAdminBroadcast(context: Context) {
    val intent = Intent("android.app.action.DEVICE_ADMIN_ENABLED")
    context.sendBroadcast(intent)
}
fun unlockDevice(context: Context) {

    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    val wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK  or PowerManager.ACQUIRE_CAUSES_WAKEUP, "tvapplication::MyWakelockTag")
//    if (!wakeLock.isHeld)
    wakeLock.acquire()

}

