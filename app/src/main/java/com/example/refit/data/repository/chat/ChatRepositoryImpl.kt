package com.example.refit.data.repository.chat

import com.example.refit.data.model.chat.Chat
import com.example.refit.data.model.chat.ChatRoom
import com.example.refit.data.model.chat.CreateRoom
import com.example.refit.data.model.chat.ResponseCreateRoom
import com.example.refit.data.repository.chat.datasource.ChatDataSource
import okhttp3.ResponseBody
import retrofit2.Call

class ChatRepositoryImpl(private val chatDataSource: ChatDataSource) : ChatRepository {

    override suspend fun rooms(accessToken: String): Call<List<ChatRoom>> {
        return chatDataSource.rooms(accessToken)
    }

    override suspend fun chats(accessToken: String, roomId: String): Call<List<Chat>> {
        return chatDataSource.chats(accessToken, roomId)
    }

    override suspend fun delete(accessToken: String, roomId: String): Call<ResponseBody> {
        return chatDataSource.delete(accessToken, roomId)
    }

    override suspend fun create(accessToken: String, request: CreateRoom): Call<ResponseCreateRoom> {
        return chatDataSource.create(accessToken, request)
    }
}