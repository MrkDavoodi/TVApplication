package com.example.tvapplication.data.local

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SharedPreferencesHelper @Inject constructor(@ApplicationContext context: Context) {

    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveDataInSharPrf( key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun getDataFromSharPrf(key: String, default: String): String {
        return prefs.getString(key, default).toString()
    }

    companion object {
        const val PREFS_NAME = "myAppPrefs"
        const val VERSION_KEY = "version"
        const val BASE_URL_KEY = "base_url"
    }
}