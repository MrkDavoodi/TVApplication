package com.example.tvapplication.time

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

//        val background = Intent(context, AlarmWorker::class.java)
        Log.i("IN AlarmReceiver","There is AlarmReceiver!!! $context")
//        context!!.startService(background)

        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            val appPackage = "com.example.tvapplication" // Replace with your app's package name
            val mainActivityClass = "com.example.tvapplication.MainActivity" // Replace with your app's main activity class

            val launchIntent = Intent(Intent.ACTION_MAIN)
            launchIntent.setClassName(appPackage, mainActivityClass)
            launchIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context?.startActivity(launchIntent)
        }

    }
}