package com.example.tvapplication.commons

import android.os.Handler
import android.os.Looper
import java.util.Date
import java.util.Timer
import java.util.TimerTask


fun mainService(sendRequest:()->Unit) {


        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                sendRequest()
                //     Schedule the task to run every 5 minutes (300,000 milliseconds)
                mainHandler.postDelayed(this, 300000)
            }
        })








//    val timer = Timer()
//    val task = object : TimerTask() {
//        override fun run() {
//            sendRequest()
//        }
//    }
//
////     Schedule the task to run every 5 minutes (300,000 milliseconds)
//    timer.schedule(task, 0, 600000)
}