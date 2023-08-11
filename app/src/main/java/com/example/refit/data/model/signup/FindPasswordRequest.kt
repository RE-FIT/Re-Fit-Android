package com.example.refit.data.model.signup

import com.google.gson.annotations.SerializedName

// 비밀번호 찾기 DTO
data class FindPasswordRequest (
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("loginId")
    val loginId: String
    )