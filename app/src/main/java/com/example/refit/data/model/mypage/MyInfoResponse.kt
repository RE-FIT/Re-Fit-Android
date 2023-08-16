package com.example.refit.data.model.mypage

import com.google.gson.annotations.SerializedName

data class MyInfoResponse(
    @SerializedName("day")
    val day: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
)