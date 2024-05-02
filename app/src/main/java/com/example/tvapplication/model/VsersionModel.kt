package com.example.tvapplication.model

import com.google.gson.annotations.SerializedName

data class VsersionModel(
    @SerializedName("Condition")
    val Condition: String,
    @SerializedName("Holyday")
    val Holyday: String,
    @SerializedName("ONTime")
    val ONTime: String,
    @SerializedName("Player")
    val Player: List<String>,
    @SerializedName("Ver")
    val Ver: Int,
    @SerializedName("VideoList")
    val VideoList: List<String>,
    @SerializedName("needToUpp")
    val needToUpp: Boolean,
    @SerializedName("onOffTimeSchedule")
    val onOffTimeSchedule: List<OnOffTimeSchedule>,
    @SerializedName("updateDate")
    val updateDate: UpdateDate,
    @SerializedName("versionApp")
    val versionApp: Double
)