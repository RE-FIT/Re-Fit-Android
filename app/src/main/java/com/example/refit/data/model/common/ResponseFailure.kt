package com.example.refit.data.model.common
import com.google.gson.annotations.SerializedName


data class ResponseFailure(
    @SerializedName("code")
    val code: Int,
    @SerializedName("errorMessage")
    val errorMessage: String
)