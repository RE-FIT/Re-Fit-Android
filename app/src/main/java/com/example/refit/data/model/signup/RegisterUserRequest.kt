package com.example.refit.data.model.signup

import com.google.gson.annotations.SerializedName

// 회원 가입용 임시 코드 (추후 삭제 처리)
data class RegisterUserRequest (
    @SerializedName("loginId")
    val loginId: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("birth")
    val birth: String,
    @SerializedName("gender")
    val gender: Int
    )