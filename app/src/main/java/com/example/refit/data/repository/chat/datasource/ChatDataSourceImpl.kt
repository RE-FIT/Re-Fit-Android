package com.example.refit.data.repository.chat.datasource

import com.example.refit.data.model.chat.Chat
import com.example.refit.data.model.chat.ChatRoom
import com.example.refit.data.model.chat.CreateRoom
import com.example.refit.data.model.chat.ResponseCreateRoom
import com.example.refit.data.network.api.ChatApi
import okhttp3.ResponseBody
import retrofit2.Call

class ChatDataSourceImpl(private val chatApi: ChatApi) : ChatDataSource {
    override suspend fun rooms(accessToken: String): Call<List<ChatRoom>> {
        return chatApi.rooms(accessToken)
    }

    override suspend fun chats(accessToken: String, roomId: String): Call<List<Chat>> {
        return chatApi.chats(accessToken, roomId)
    }

    override suspend fun delete(accessToken: String, roomId: String): Call<ResponseBody> {
        return chatApi.delete(accessToken, roomId)
    }

    override suspend fun create(accessToken: String, request: CreateRoom): Call<ResponseCreateRoom> {
        return chatApi.create(accessToken, request)
    }
}