package com.example.refit.data.network.api

import com.example.refit.data.model.mypage.CheckNicknameResponse
import com.example.refit.data.model.mypage.NicknameCheckRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MypageApi {
    @POST("check_nickname")
    fun checkNicknameAvailability(@Body request: NicknameCheckRequest): Call<CheckNicknameResponse>

//    @GET()
}