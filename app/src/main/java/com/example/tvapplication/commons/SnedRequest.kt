package com.example.tvapplication.commons

import java.util.Date
import java.util.Timer
import java.util.TimerTask

fun sendRequest() {
    // Code to send the request goes here
    println("Sending request at: ${Date()}")
}

fun mainService(sendRequest:()->Unit) {
    val timer = Timer()
    val task = object : TimerTask() {
        override fun run() {
            sendRequest()
        }
    }

//     Schedule the task to run every 5 minutes (300,000 milliseconds)
    timer.schedule(task, 0, 300000)
}