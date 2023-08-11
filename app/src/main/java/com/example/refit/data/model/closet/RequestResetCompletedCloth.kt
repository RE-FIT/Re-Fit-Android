package com.example.refit.data.model.closet

import com.google.gson.annotations.SerializedName

data class RequestResetCompletedCloth(
    @SerializedName("season")
    val season: Int,
    @SerializedName("targetCnt")
    val targetCnt: Int?,
    @SerializedName("targetPeriod")
    val targetPeriod: Int?,
    @SerializedName("isPlan")
    val isPlan: Boolean,
    @SerializedName("cntPerMonth")
    val cntPerMonth: Int?,
    @SerializedName("cntPerWeek")
    val cntPerWeek: Int?
)
