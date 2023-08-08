package com.example.refit.data.network.api

import com.example.refit.data.model.closet.RequestAddNewCloth
import com.example.refit.data.model.closet.RequestRegisteredClothes
import com.example.refit.data.model.closet.RequestResetCompletedCloth
import com.example.refit.data.model.closet.ResponseAddNewCloth
import com.example.refit.data.model.closet.ResponseRegisteredClothInfo
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
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @GET("/refit/clothe/{id}")
    fun getRegisteredClothInfo(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<ResponseRegisteredClothInfo>

    @PUT("/refit/clothe/{id}")
    fun fixClothItem(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body request: RequestAddNewCloth
    ): Call<Void>

    @PATCH("/refit/clothe/{id}")
    fun resetCompletedCloth(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body request: RequestResetCompletedCloth
    ): Call<Void>

    @PATCH("/refit/clothe/{id}/wear")
    fun wearClothes(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): Call<Void>

}