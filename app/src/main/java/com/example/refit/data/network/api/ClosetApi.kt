package com.example.refit.data.network.api

import com.example.refit.data.model.closet.ResponseAddNewCloth
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ClosetApi {
    @Multipart
    @POST("/refit/clothe")
    fun addNewCloth(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
        @Part("request") request: RequestBody
    ): Call<Long>

}