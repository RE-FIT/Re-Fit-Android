package com.example.refit.data.model.mypage

import com.google.gson.annotations.SerializedName

data class PasswordUpdateResponse (
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String
)