package com.example.refit.data.model.mypage

import com.google.gson.annotations.SerializedName

data class UpdateDTO (
    @SerializedName("name")
    val name: String,
    @SerializedName("birth")
    val birth: String,
    @SerializedName("gender")
    val gender: Int
)