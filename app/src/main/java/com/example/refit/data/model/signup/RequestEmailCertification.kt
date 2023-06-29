package com.example.refit.data.model.signup
import com.google.gson.annotations.SerializedName


data class RequestEmailCertification(
    @SerializedName("email")
    val email: String
)