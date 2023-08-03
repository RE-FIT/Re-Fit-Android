package com.example.refit.data.model.mypage

import com.google.gson.annotations.SerializedName

data class CheckNicknameResponse(
    @SerializedName(value = "Checked")
    val Checked: Boolean
)