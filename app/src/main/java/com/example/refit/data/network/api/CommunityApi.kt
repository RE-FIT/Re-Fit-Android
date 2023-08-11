package com.example.refit.data.network.api

import com.example.refit.data.model.community.BlockDto
import com.example.refit.data.model.community.PostResponse
import com.example.refit.data.model.community.Member
import com.example.refit.data.model.community.ReportedUser
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @Multipart
    @PUT("refit/community/{postId}/update")
    fun modifyPostIncludeImage(
        @Header("Authorization") accessToken: String,
        @Path ("postId") postId: Int,
        @Part ("postDto") postDto: RequestBody,
        @Part ("image_updated") image_updated: Boolean,
        @Part image: List<MultipartBody.Part>
    ): Call<ResponseBody>

    @Multipart
    @PUT("refit/community/{postId}/update")
    fun modifyPost(
        @Header ("Authorization") accessToken: String,
        @Path ("postId") postId: Int,
        @Part ("postDto") postDto: RequestBody,
        @Part ("image_updated") image_updated: Boolean,
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

    @DELETE("refit/community/{postId}")
    fun deletePost(
        @Header("Authorization") accessToken: String,
        @Path("postId") postId: Int
    ) : Call<ResponseBody>

    @PATCH("refit/community/{postId}")
    fun changePostStatus(
        @Header("Authorization") accessToken: String,
        @Path("postId") postId: Int
    ): Call<PostResponse>


    @POST("refit/community/{postId}/scrap")
    fun scrapPost(
        @Header("Authorization") accessToken: String,
        @Path("postId") postId: Int
    ): Call<ResponseBody>

    @POST("refit/block")
    fun blockUser(
        @Header("Authorization") accessToken: String,
        @Body requestBody: BlockDto,
    ): Call<ResponseBody>

    @POST("refit/report")
    fun reportUser(
        @Header("Authorization") accessToken: String,
        @Body requestBody: ReportedUser
    ): Call<ResponseBody>


}