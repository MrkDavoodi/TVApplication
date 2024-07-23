package com.example.tvapplication.data

import android.app.Application
import com.example.tvapplication.data.local.SharedPreferencesHelper
import com.example.tvapplication.data.local.SharedPreferencesHelper.Companion.BASE_URL_KEY
import javax.inject.Inject

object ApiURL {

    const val BASE_URL = "https://kiyandigi.com/"
    const val URL_VERSION = "MRKSignage/Version.json"
    const val URL_VIDEO = "${BASE_URL}MRKSignage/Video/"
    const val URL_VIDEO2 = "https://mrk.co.ir/"


}