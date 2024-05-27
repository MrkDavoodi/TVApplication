package com.example.tvapplication.launcher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.tvapplication.commons.putDeviceToSleep
import com.example.tvapplication.commons.sendDeviceAdminBroadcast
import com.example.tvapplication.commons.unlockDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


open class BootCompletedReceiver : BroadcastReceiver() {
    private val broadcastReceiverScope = CoroutineScope(SupervisorJob())

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            if (intent != null) {
                if (intent.action == "gotoTurnOf") {
                    if (context != null){
                        putDeviceToSleep(context)
                        sendDeviceAdminBroadcast(context)
                    }
                }
                if (intent.action == "gotoTurnOn") {
                    if (context != null) {
                        unlockDevice(context)
                        sendDeviceAdminBroadcast(context)
//                        val i = Intent(context.applicationContext, MainActivity::class.java)
//                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                        context.startActivity(i)
                    }

                }

                if (intent.action == "android.intent.action.BOOT_COMPLETED") {
                    val launchIntent =
                        context?.packageManager?.getLaunchIntentForPackage("com.example.tvapplication")
                    launchIntent?.let {
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(it)
                    }

                }
            }
        } catch (e: Exception) {
            Log.i("inReceiver", "This is message: ${e.message}")
        }
    }

}






