package com.example.refit.data.model.chat

import com.google.gson.annotations.SerializedName
import java.time.Instant

//거래 완료 DTO
data class Trade(
    @SerializedName("postId")
    val postId: Int,
    @SerializedName("username")
    val username: String,
)