package com.example.tvapplication.model.version

import com.google.gson.annotations.SerializedName

data class OffTime1(
    @SerializedName("hour")
    val hour: String,
    @SerializedName("minute")
    val minute: String
)