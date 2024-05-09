package com.example.tvapplication.time

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val launchIntent = context.packageManager?.getLaunchIntentForPackage("com.example.tvapplication")
        launchIntent?.let {
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(it)
        }
    }
}