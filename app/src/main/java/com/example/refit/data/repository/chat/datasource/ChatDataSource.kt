package com.example.refit.data.repository.chat.datasource

import com.example.refit.data.model.chat.Chat
import com.example.refit.data.model.chat.ChatRoom
import com.example.refit.data.model.chat.CreateRoom
import com.example.refit.data.model.chat.ResponseCreateRoom
import okhttp3.ResponseBody
import retrofit2.Call

interface ChatDataSource {

    suspend fun rooms (accessToken: String): Call<List<ChatRoom>>

    suspend fun chats (accessToken: String, roomId: String): Call<List<Chat>>

    suspend fun delete (accessToken: String, roomId: String): Call<ResponseBody>

    suspend fun create (accessToken: String, request: CreateRoom): Call<ResponseCreateRoom>
}