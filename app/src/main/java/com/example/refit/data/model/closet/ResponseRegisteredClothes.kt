package com.example.refit.data.model.closet
import com.google.gson.annotations.SerializedName


data class ResponseRegisteredClothes(
    @SerializedName("cntPerMonth")
    val cntPerMonth: Int,
    @SerializedName("cntPerWeek")
    val cntPerWeek: Int,
    @SerializedName("count")
    val count: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("remainedDay")
    val remainedDay: Int,
    @SerializedName("targetCnt")
    val targetCnt: Int,
    @SerializedName("lastDate")
    val lastDate: String?
)