package com.example.tvapplication.model.version

import com.google.gson.annotations.SerializedName

data class UpdateDate(
    @SerializedName("day")
    val day: String,
    @SerializedName("hour")
    val hour: String,
    @SerializedName("minute")
    val minute: String,
    @SerializedName("month")
    val month: String,
    @SerializedName("setDate")
    val setDate: Boolean,
    @SerializedName("year")
    val year: String
)