package com.example.tvapplication.model

import com.google.gson.annotations.SerializedName

data class OnTime(
    @SerializedName("hour")
    val hour: String,
    @SerializedName("minute")
    val minute: String
)