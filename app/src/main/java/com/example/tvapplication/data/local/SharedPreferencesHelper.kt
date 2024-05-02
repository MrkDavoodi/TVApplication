package com.example.tvapplication.data.local

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesHelper {
    private const val PREFS_NAME = "myAppPrefs"
    private const val VERSION_KEY = "version"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveVersionNumber(context: Context, version: String) {
        val prefs = getSharedPreferences(context)
        prefs.edit().putString(VERSION_KEY, version).apply()
    }

    fun getVersionNumber(context: Context): String {
        val prefs = getSharedPreferences(context)
        return prefs.getString(VERSION_KEY, "0").toString()
    }
}