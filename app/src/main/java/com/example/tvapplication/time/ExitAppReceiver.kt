package com.example.tvapplication.time

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ExitAppReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "EXIT_APP_ACTION") {
            // Close all activities in the app
            (context as? Activity)?.finishAffinity()
        }
    }
}