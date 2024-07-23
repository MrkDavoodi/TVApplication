package com.example.tvapplication.di

import com.example.tvapplication.data.local.SharedPreferencesHelper
import javax.inject.Inject

class DynamicBaseUrlProvider @Inject constructor(sharedPreferences: SharedPreferencesHelper) : BaseUrlPro {

    private var baseUrl: String =sharedPreferences.getDataFromSharPrf(
        SharedPreferencesHelper.BASE_URL_KEY,
        "https://mrk.co.ir/"
    )

    fun setBaseUrl(baseUrl: String) {
        this.baseUrl = baseUrl
    }

    override fun getBaseUrl(): String {
        return baseUrl
    }
}

