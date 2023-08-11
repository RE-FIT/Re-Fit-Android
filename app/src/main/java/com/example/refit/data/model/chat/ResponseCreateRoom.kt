package com.example.refit.data.model.chat

import com.google.gson.annotations.SerializedName
import java.time.Instant

//채팅 DTO
data class ResponseCreateRoom(
    @SerializedName("roomId")
    val roomId: Int,
)