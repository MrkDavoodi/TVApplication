package com.example.tvapplication.model.version

import com.google.gson.annotations.SerializedName

data class OnOffTimeSchedule(
    @SerializedName("day")
    val day: String,
    @SerializedName("onTime1")
    val onTime1: OnTime?,
    @SerializedName("onTime1")
    val offTime1: OnTime?
)


