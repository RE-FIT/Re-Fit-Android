package com.example.refit.data.network.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CommunityApi {

    @GET("refit/community")
    fun loadCommunityList(
        @Header("Authorization") accessToken: String,
        @Query("postType") postType: Int,
        @Query("gender") gender: Int,
        @Query("category") category: Int,
        @Query("region") region: String
    ): Call<ResponseBody>
}