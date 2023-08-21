package com.example.refit.data.model.mypage

import com.google.gson.annotations.SerializedName

data class ShowMyInfoResponse (
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("email") val email: String,
    @SerializedName("loginId") val loginId: String,
    @SerializedName("name") val name: String,
    @SerializedName("birth") val birth: String,
    @SerializedName("gender") val gender: Int,
    @SerializedName("type") val type: String
)