package com.example.refit.data.model.chat

import com.google.gson.annotations.SerializedName
import java.time.Instant

//채팅 DTO
data class Chat(
    @SerializedName("content")
    val content: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("isMy")
    val isMy: Boolean,
    @SerializedName("notificationId")
    val notificationId: String
)