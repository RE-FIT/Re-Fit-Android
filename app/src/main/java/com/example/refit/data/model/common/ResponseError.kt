package com.example.refit.data.model.common
import com.google.gson.annotations.SerializedName


data class ResponseError(
    @SerializedName("code")
    val code: Int,
    @SerializedName("errorMessage")
    val errorMessage: String
)