package com.example.tvapplication.time

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.example.tvapplication.MainActivity


class BackgroundService : Service() {
    private var isRunning = false
    private var context: Context? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        context = this
        isRunning = false
        this.run { myTask }
    }

    private val myTask = Runnable {
        // Do something here
        val i = Intent(applicationContext, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext?.startActivity(i)
        stopSelf()
    }

    override fun onDestroy() {
        isRunning = false
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (!isRunning) {
            isRunning = true
            this.myTask.run()
        }
        return START_STICKY
    }
}