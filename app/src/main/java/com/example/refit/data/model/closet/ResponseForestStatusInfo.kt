package com.example.refit.data.model.closet
import com.google.gson.annotations.SerializedName
data class ResponseForestStatusInfo(
    @SerializedName("count")
    val count: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("remainedCnt")
    val remainedCnt: Int,
    @SerializedName("targetCnt")
    val targetCnt: Int
)