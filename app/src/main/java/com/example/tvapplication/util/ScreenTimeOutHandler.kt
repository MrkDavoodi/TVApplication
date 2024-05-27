package com.example.tvapplication.util

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
        val wl = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "tvapplication:tagOnTime")
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
            manager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "tvapplication:tagOffTime")
        if (isScreenOn)
            wl.acquire(offTime)
        wl.release()
    }

}

