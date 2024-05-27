package com.example.tvapplication.commons

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
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
        "eed" -> Calendar.WEDNESDAY
        "thu" -> Calendar.THURSDAY
        "fri" -> Calendar.FRIDAY
        "sat" -> Calendar.SATURDAY
        "sun" -> Calendar.SUNDAY
        else -> 0
    }
}

fun openActivity(mContext: Context?) {

    val launcherIntent = Intent()
        .setAction(Intent.ACTION_BOOT_COMPLETED)
        .addCategory(Intent.CATEGORY_LAUNCHER)
    var possibleBrowsers: List<ResolveInfo>
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        possibleBrowsers = mContext?.packageManager?.queryIntentActivities(
            launcherIntent,
            PackageManager.MATCH_DEFAULT_ONLY
        )!!
        if (possibleBrowsers.isEmpty()) {
            possibleBrowsers = mContext.packageManager?.queryIntentActivities(
                launcherIntent,
                PackageManager.MATCH_ALL
            )!!
        }
        Log.i("In Receiver","This is possibleBrowsers : $possibleBrowsers")

    } else {
        possibleBrowsers = mContext?.packageManager?.queryIntentActivities(
            launcherIntent,
            PackageManager.MATCH_DEFAULT_ONLY
        )!!
    }


    if (possibleBrowsers.isNotEmpty()) {
        val launchIntent =
            mContext.packageManager?.getLaunchIntentForPackage(possibleBrowsers[0].activityInfo.packageName)
        launchIntent?.let {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            mContext.startActivity(it)
        }

    } else {
        val browserIntent2 = Intent(Intent.ACTION_BOOT_COMPLETED)
        mContext.startActivity(browserIntent2)
    }
}

fun putDeviceToSleep(context: Context) {
    val devicePolicyManager = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    val componentName = ComponentName(context, MyDeviceAdminReceiver::class.java)
    if (!devicePolicyManager.isAdminActive(componentName)) {
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "دریافت مجوز ")
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
    val devicePolicyManager = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    val adminComponentName = ComponentName(context, MyDeviceAdminReceiver::class.java)

    if (devicePolicyManager.isAdminActive(adminComponentName)) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            devicePolicyManager.lockNow(DevicePolicyManager.FLAG_EVICT_CREDENTIAL_ENCRYPTION_KEY)
        }

    }
    if (!devicePolicyManager.isAdminActive(adminComponentName)) {
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponentName)
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "دریافت مجوز ")
        context.startActivity(intent)
    }
}

