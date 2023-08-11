package com.example.refit.data.repository.chat

import com.example.refit.data.model.chat.Chat
import com.example.refit.data.model.chat.ChatRoom
import okhttp3.ResponseBody
import retrofit2.Call

interface ChatRepository {
    suspend fun rooms (accessToken: String): Call<List<ChatRoom>>

    suspend fun chats (accessToken: String, roomId: String): Call<List<Chat>>

    suspend fun delete (accessToken: String, roomId: String): Call<ResponseBody>
}