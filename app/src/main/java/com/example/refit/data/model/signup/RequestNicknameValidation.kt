package com.example.refit.data.model.signup

import com.google.gson.annotations.SerializedName

data class RequestNicknameValidation(
    @SerializedName("name")
    val name: String
)
