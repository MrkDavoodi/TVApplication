package com.example.tvapplication.time

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.tvapplication.MainActivity


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

//        val background = Intent(context, AlarmWorker::class.java)
        Log.i("IN AlarmReceiver","There is AlarmReceiver!!! $context")
//        context!!.startService(background)

        val i = Intent(context, MainActivity::class.java)
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(i)

    }
}