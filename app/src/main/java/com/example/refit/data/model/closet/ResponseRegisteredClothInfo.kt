package com.example.refit.data.model.closet
import com.google.gson.annotations.SerializedName


data class ResponseRegisteredClothInfo(
    @SerializedName("category")
    val category: Int,
    @SerializedName("cntPerMonth")
    val cntPerMonth: Int,
    @SerializedName("cntPerWeek")
    val cntPerWeek: Int,
    @SerializedName("id")
    val id: Long,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("isPlan")
    val isPlan: Boolean,
    @SerializedName("season")
    val season: Int,
    @SerializedName("targetCnt")
    val targetCnt: Int,
    @SerializedName("targetPeriod")
    val targetPeriod: Int
)