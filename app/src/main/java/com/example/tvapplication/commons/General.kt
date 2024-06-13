package com.example.tvapplication.commons

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.PowerManager
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.example.tvapplication.MainActivity
import com.example.tvapplication.admin.MyDeviceAdminReceiver
import java.io.File
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
    val devicePolicyManager =
        context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    val componentName = ComponentName(context, MyDeviceAdminReceiver::class.java)
    if (!devicePolicyManager.isAdminActive(componentName)) {
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "دریافت مجوز ")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
    if (devicePolicyManager.isAdminActive(componentName)) {
        devicePolicyManager.lockNow()
    }
}

fun sendDeviceAdminBroadcast(context: Context) {
    val intent = Intent("android.app.action.DEVICE_ADMIN_ENABLED")
    context.sendBroadcast(intent)
}

fun turnOffScreen(context: Context) {

    val wakeLock: PowerManager.WakeLock =
        (context.getSystemService(Context.POWER_SERVICE) as PowerManager).run {
            newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK,
                "MRKSignagePlayer::MyWakelockTag"
            ).apply {
                acquire()
            }
        }
    wakeLock.release()
}

fun unlockDevice(context: Context) {

    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    val wakeLock = powerManager.newWakeLock(
        PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
        "tvapplication::MyWakelockTag"
    )
//    if (!wakeLock.isHeld)
    wakeLock.acquire()
//    wakeLock.release()

}

fun getAdminPermission(context: Context) {
    val devicePolicyManager =
        context.getSystemService(Activity.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    val componentName = ComponentName(context, MyDeviceAdminReceiver::class.java)
    if (!devicePolicyManager.isAdminActive(componentName)) {
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "دریافت مجوز ")
//        intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}

fun checkStoragePermissions(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        //Android is 11 (R) or above
        Environment.isExternalStorageManager()
    } else {
        val write =
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        val read =
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )

        return read == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED
    }
}

fun getPermission(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        if (!Settings.canDrawOverlays(context)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$context.packageName")
            )
            val requestCode = 123

            (context as Activity).startActivityForResult(intent, requestCode)
        }

}

fun deleteLocalFileIfNotExistInVideosFromRemote(fileName: String) {
    try {
        val videoFile = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "/Folder/$fileName.mp4"
        )
        if (videoFile.exists()) {
            val result = videoFile.delete()
            if (result) {
                println("File deleted successfully.")
            } else {
                println("Error occurred while deleting the file or file does not exist.")
            }

        }
    } catch (e: Exception) {
        Log.d("ExceptionDELETE", e.message.toString())

    }

}

