package com.example.refit.data.model.signup

import com.google.gson.annotations.SerializedName

// 아이디 찾기 DTO
data class FindIdResponse (
    @SerializedName("loginId")
    val loginId: String,
)