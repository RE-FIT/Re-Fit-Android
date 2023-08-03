package com.example.refit.data.network.api

import com.example.refit.data.model.community.PostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.IOException


interface CommunityApi {

    @GET("refit/community")
    fun loadCommunityList(
        @Header("Authorization") accessToken: String,
        @Query("postType") postType: Int = 0,
        @Query("gender") gender: Int = 0,
        @Query("category") category: Int = 0,
    ): Call<ResponseBody>

    @Multipart
    @POST("refit/community")
    fun createPost(
        @Header("Authorization") accessToken: String,
        @Part ("postDto") postDto: RequestBody,
        @Part image: List<MultipartBody.Part>
    ): Call<ResponseBody>

    @GET("refit/community/{postId}")
    fun getPost(
        @Header("Authorization") accessToken: String,
        @Path("postId") postId: Int
    ): Call<PostResponse>

    @GET("refit/community/search")
    fun loadSearchResult(
        @Header("Authorization") accessToken: String,
        @Query("keyword") postType: String,
    ): Call<ResponseBody>

}