package com.example.tvapplication.util

import android.app.Service
import android.content.Intent
import android.os.IBinder

class RestartService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Restart the app here.
        // You can close all activities and restart the main activity.
        val restartIntent = packageManager.getLaunchIntentForPackage(packageName)
        restartIntent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(restartIntent)

        // Stop the service since it has served its purpose.
        stopSelf()

        return START_NOT_STICKY
    }
}