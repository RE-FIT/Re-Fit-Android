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

    // # 커뮤니티 초기 화면 API 메서드
    @GET("refit/community")
    fun initCommunityList(
        @Header("Authorization") accessToken: String,
    ): Call<ResponseBody>

    @GET("refit/community")
    fun loadCommunityList(
        @Header("Authorization") accessToken: String,
        @Query("postType") postType: Int = 0,
        @Query("gender") gender: Int = 0,
        @Query("category") category: Int = 0,
    ): Call<ResponseBody>

    @GET("refit/community")
    fun loadCommunityListOnlyPostType(
        @Header("Authorization") accessToken: String,
        @Query("postType") postType: Int = 0,
    ): Call<ResponseBody>

    @GET("refit/community")
    fun loadCommunityListOnlyGender(
        @Header("Authorization") accessToken: String,
        @Query("gender") gender: Int = 0,
    ): Call<ResponseBody>

    @GET("refit/community")
    fun loadCommunityListOnlyCategory(
        @Header("Authorization") accessToken: String,
        @Query("category") category: Int = 0,
    ): Call<ResponseBody>

    @GET("refit/community")
    fun loadCommunityListPTAndGender(
        @Header("Authorization") accessToken: String,
        @Query("postType") postType: Int = 0,
        @Query("gender") gender: Int = 0,
    ): Call<ResponseBody>

    @GET("refit/community")
    fun loadCommunityListPTAndCategory(
        @Header("Authorization") accessToken: String,
        @Query("postType") postType: Int = 0,
        @Query("category") category: Int = 0,
    ): Call<ResponseBody>

    @GET("refit/community")
    fun loadCommunityListGenderAndCategory(
        @Header("Authorization") accessToken: String,
        @Query("gender") gender: Int = 0,
        @Query("category") category: Int = 0,
    ): Call<ResponseBody>

    // # 글 등록 POST API 메서드
    @Multipart
    @POST("refit/community")
    fun createPost(
        @Header("Authorization") accessToken: String,
        @Part("postDto") postDto: RequestBody,
        @Part image: List<MultipartBody.Part>
    ): Call<ResponseBody>


    // # 글 수정 PUT API 메서드
    @Multipart
    @PUT("refit/community/{postId}/update")
    fun modifyPostIncludeImage(
        @Header("Authorization") accessToken: String,
        @Path("postId") postId: Int,
        @Part("postDto") postDto: RequestBody,
        @Part("image_updated") image_updated: Boolean,
        @Part image: List<MultipartBody.Part>
    ): Call<ResponseBody>

    @Multipart
    @PUT("refit/community/{postId}/update")
    fun modifyPost(
        @Header("Authorization") accessToken: String,
        @Path("postId") postId: Int,
        @Part("postDto") postDto: RequestBody,
        @Part("image_updated") image_updated: Boolean,
    ): Call<ResponseBody>

    // # 글 조회 GET API 메서드
    @GET("refit/community/{postId}")
    fun getPost(
        @Header("Authorization") accessToken: String,
        @Path("postId") postId: Int
    ): Call<PostResponse>

    // # 검색 조회 GET API 메서드
    @GET("refit/community/search")
    fun loadSearchResult(
        @Header("Authorization") accessToken: String,
        @Query("keyword") keyword: String,
    ): Call<ResponseBody>

    @GET("refit/community/search")
    fun loadSearchResultAll(
        @Header("Authorization") accessToken: String,
        @Query("keyword") keyword: String,
        @Query("postType") postType: Int = 0,
        @Query("gender") gender: Int = 0,
        @Query("category") category: Int = 0,
    ): Call<ResponseBody>

    @GET("refit/community/search")
    fun loadSearchResulttOnlyPostType(
        @Header("Authorization") accessToken: String,
        @Query("keyword") keyword: String,
        @Query("postType") postType: Int = 0,
    ): Call<ResponseBody>

    @GET("refit/community/search")
    fun loadSearchResultOnlyGender(
        @Header("Authorization") accessToken: String,
        @Query("keyword") keyword: String,
        @Query("gender") gender: Int = 0,
    ): Call<ResponseBody>

    @GET("refit/community/search")
    fun loadSearchResultOnlyCategory(
        @Header("Authorization") accessToken: String,
        @Query("keyword") keyword: String,
        @Query("category") category: Int = 0,
    ): Call<ResponseBody>

    @GET("refit/community/search")
    fun loadSearchResultPTAndGender(
        @Header("Authorization") accessToken: String,
        @Query("keyword") keyword: String,
        @Query("postType") postType: Int = 0,
        @Query("gender") gender: Int = 0,
    ): Call<ResponseBody>

    @GET("refit/community/search")
    fun loadSearchResultPTAndCategory(
        @Header("Authorization") accessToken: String,
        @Query("keyword") keyword: String,
        @Query("postType") postType: Int = 0,
        @Query("category") category: Int = 0,
    ): Call<ResponseBody>

    @GET("refit/community")
    fun loadSearchResultGenderAndCategory(
        @Header("Authorization") accessToken: String,
        @Query("keyword") keyword: String,
        @Query("gender") gender: Int = 0,
        @Query("category") category: Int = 0,
    ): Call<ResponseBody>

    // # 글 삭제 DELETE API 메서드
    @DELETE("refit/community/{postId}")
    fun deletePost(
        @Header("Authorization") accessToken: String,
        @Path("postId") postId: Int
    ): Call<ResponseBody>

    // # 글 상태 변경 PATCH API 메서드
    @PATCH("refit/community/{postId}")
    fun changePostStatus(
        @Header("Authorization") accessToken: String,
        @Path("postId") postId: Int
    ): Call<PostResponse>


    // # 글 스크랩 POST API 메서드
    @POST("refit/community/{postId}/scrap")
    fun scrapPost(
        @Header("Authorization") accessToken: String,
        @Path("postId") postId: Int
    ): Call<ResponseBody>

    // # 사용자 차단 POST API 메서드
    @POST("refit/block")
    fun blockUser(
        @Header("Authorization") accessToken: String,
        @Body requestBody: BlockDto,
    ): Call<ResponseBody>

    // # 신고 POST API 메서드
    @POST("refit/report")
    fun reportUser(
        @Header("Authorization") accessToken: String,
        @Body requestBody: ReportedUser
    ): Call<ResponseBody>


}