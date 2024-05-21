package com.example.tvapplication.launcher

import android.content.Context
import android.content.Intent

class Utils {

    companion object {
        fun startActivity(context: Context, clazz: Class<*>) {

            val intent = Intent(context, clazz)

            // start your next activity

            context.startActivity(intent)

        }
    }

}