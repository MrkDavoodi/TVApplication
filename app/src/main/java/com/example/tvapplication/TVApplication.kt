package com.example.tvapplication

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.tvapplication.data.local.SharedPreferencesHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TVApplication:Application() {
}