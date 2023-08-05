package com.example.refit.data.network.api

import com.example.refit.data.model.closet.RequestRegisteredClothes
import com.example.refit.data.model.closet.ResponseAddNewCloth
import com.example.refit.data.model.closet.ResponseRegisteredClothes
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ClosetApi {
    @Multipart
    @POST("/refit/clothe")
    fun addNewCloth(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
        @Part("request") request: RequestBody
    ): Call<Long>

//    @GET("/refit/clothe")
    @GET("/refit/clothe")
    fun getRegisteredClothes(
        @Header("Authorization") token: String,
        @Query("category") category: Int,
        @Query("season") season: Int,
        @Query("sort") sort: String
    ): Call<List<ResponseRegisteredClothes>>

    @DELETE("/refit/clothe/{id}")
    fun deleteClothItem(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<Void>

}