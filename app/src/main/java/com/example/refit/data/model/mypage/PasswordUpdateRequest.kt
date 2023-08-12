package com.example.refit.data.model.mypage

import com.google.gson.annotations.SerializedName
data class PasswordUpdateRequest(
    @SerializedName("currentPassword") val currentPassword: String?,
    @SerializedName("newPassword") val newPassword: String?
)