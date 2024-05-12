package com.example.tvapplication.launcher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED ) {
            // Start the desired app here
            val launchIntent = context?.packageManager?.getLaunchIntentForPackage("com.example.tvapplication")
            launchIntent?.let {
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(it)
            }
        }
        if (intent?.action == "EXIT_APP_ACTION") {
            // Close all activities in the app
            Toast.makeText(context, "Action: " + intent.action, Toast.LENGTH_SHORT).show()
//            exitProcess(0)
//            (context as? Activity)?.finishAffinity()
        }
    }
}

