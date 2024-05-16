package com.example.tvapplication.launcher

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AlarmBroadcastReceiver : BroadcastReceiver() {
    private val broadcastReceiverScope = CoroutineScope(SupervisorJob())

    override fun onReceive(p0: Context?, p1: Intent?) {
        val pendingResult: PendingResult = goAsync()
        broadcastReceiverScope.launch(Dispatchers.Default) {
            try {
                p1?.let { intent ->
                    when (intent.action) {
                        "android.intent.action.BOOT_COMPLETED" -> {
                            val launchIntent =
                                p0?.packageManager?.getLaunchIntentForPackage("com.example.tvapplication")
                            launchIntent?.let {
                                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                p0.startActivity(it)
                            }
                        }
                        "BOOT_COMPLETED" -> {
                            val launchIntent =
                                p0?.packageManager?.getLaunchIntentForPackage("com.example.tvapplication")
                            launchIntent?.let {
                                p0.startActivity(it)
                            }
                        }

                        "EXIT_APP_ACTION" -> {
                            // Close all activities in the app
                            Toast.makeText(p0, "Action: " + intent.action, Toast.LENGTH_SHORT)
                                .show()
//                            exitProcess(0)
                            (p0 as? Activity)?.finishAffinity()
                        }

                        else -> {

                        }
                    }
                }
            } finally {
                pendingResult.finish()
                broadcastReceiverScope.cancel()
            }
        }
    }
}
