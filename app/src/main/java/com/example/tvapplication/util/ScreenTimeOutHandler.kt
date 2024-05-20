package com.example.tvapplication.util

import android.app.admin.DevicePolicyManager
import android.content.Context
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.util.Log


class ScreenTimeOutHandler(val context: Context) {
    private val mPowerManager: PowerManager? = null
    private var mWakeLock: WakeLock? = null
    private val manager = context.getSystemService(Context.POWER_SERVICE) as PowerManager

    fun turnOnScreen(onTime: Long) {
        // turn on screen
        val wl = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "AppName:tagOnTime")
        Log.v("ProximityActivity", "ON! $wl.isHeld")
        if (!wl.isHeld)
            wl.acquire(onTime)
        else
            wl.release()
    }

    fun turnOffScreen(offTime: Long) {
        // turn off screen
        Log.v("MainActivity", "OFF!")
        val isScreenOn: Boolean = manager.isInteractive

        val wl =
            manager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "AppName:tagOffTime")
        if (isScreenOn)
            wl.acquire(offTime)
//        wl.release()
    }
}

fun screenHandler(context: Context) {
    // Check if the screen is off
    val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager?
    val isScreenOn = pm!!.isScreenOn

    if (!isScreenOn) {
        val policyManager =
            context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager?
//        val adminReceiver = ComponentName(context, MyDeviceAdminReceiver::class.java)
//
//        // Check if the app has admin privileges
//        if (policyManager!!.isAdminActive(adminReceiver)) {
//            policyManager.lockNow()
//        }
    }
}