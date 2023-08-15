package com.example.refit.data.model.chat

import com.google.gson.annotations.SerializedName

//채팅방 DTO
data class ChatRoom(
    @SerializedName("roomId")
    val roomId: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("other")
    val other: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("remain")
    val remain: Int,
    @SerializedName("participants")
    val participants: List<String>,
    @SerializedName("seller")
    val seller: String,
    @SerializedName("postType")
    val postType: Int,
)