package com.example.refit.data.network.api

import com.example.refit.data.model.chat.Chat
import com.example.refit.data.model.chat.ChatRoom
import com.example.refit.data.model.chat.CreateRoom
import com.example.refit.data.model.chat.ResponseCreateRoom
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatApi {

    /**
     * 모든 체팅 방 API
     * */
    @GET("/chat/room/all")
    @Headers("content-type: application/json")
    fun rooms(@Header("Authorization") accessToken: String):Call<List<ChatRoom>>

    /**
     * 체팅 방 API
     * */
    @GET("/chat/room/{roomId}")
    @Headers("content-type: application/json")
    fun chats(@Header("Authorization") accessToken: String, @Path("roomId") roomId:String):Call<List<Chat>>

    /**
     * 체팅 방 나가기 API
     * */
    @DELETE("/chat/room/{roomId}/leave")
    @Headers("content-type: application/json")
    fun delete(@Header("Authorization") accessToken: String, @Path("roomId") roomId:String):Call<ResponseBody>

    /**
     * 체팅 방 만들기 API
     * */
    @POST("/chat/room/create")
    @Headers("content-type: application/json")
    fun create(@Header("Authorization") accessToken: String, @Body request:CreateRoom):Call<ResponseCreateRoom>
}