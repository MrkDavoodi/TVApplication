package com.example.tvapplication.workerManager.worker

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.tvapplication.MainActivity

class AlarmCheckerWorker (
    ctx: Context,
    params: WorkerParameters,
) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        return try {
            val i = Intent(applicationContext, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(i)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}

const val ALARM_CHECKER_TAG = "alarmCheckerTag"
class AlarmWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        return try {

            val i = Intent(applicationContext, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(i)
            Log.i("IN doWork","There is doWork!!! $applicationContext")
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}