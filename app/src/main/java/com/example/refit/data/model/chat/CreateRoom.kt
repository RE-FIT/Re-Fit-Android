package com.example.refit.data.model.chat

import com.google.gson.annotations.SerializedName
import java.time.Instant

//채팅 DTO
data class CreateRoom(
    @SerializedName("other")
    val other: String,
    @SerializedName("postId")
    val postId: Int,
    @SerializedName("postType")
    val postType: Int,
)