package com.example.refit.data.network.api

import androidx.lifecycle.LiveData
import com.example.refit.data.model.community.CommunityListItemResponse
import com.example.refit.data.model.mypage.CheckNicknameResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part
import retrofit2.http.Query

interface MyPageApi {
    // 회원 정보 조회
    @GET("/refit/mypage/info")
    fun showInfo(
        @Header("Authorization") token: String,
        @Field("userId") userId : Int,
        @Field("postType") postType : String
    )

    // 회원 정보 수정
    @Multipart
    @PATCH("/refit/mypage/info")
    fun updateInfo(
        @Header("Authorization") token: String,
        @Part("image") image: MultipartBody.Part?,
        @Part("name") name: RequestBody,
        @Part("birth") birth: RequestBody,
        @Part("gender") gender: RequestBody,
    )

    // 이름(닉네임) 중복 확인
    @GET("/refit/mypage/info/check")
    fun showInfo(
        @Header("Authorization") token: String,
        @Query("name") name: LiveData<String>
    ): Call<CheckNicknameResponse>

    // 내 피드 나눔
    @GET("/refit/mypage/myfeed/give")
    fun showMyFeedGive(
        @Field("postId") postId: Int,
        @Field("title") title: String,
        @Field("imgUrl") imgUrl: String,
        @Field("gender") gender: Int,
        @Field("deliveryType") deliveryType: Int,
        @Field("region") region: String,
        @Field("price") price: Int,
    ): Call<CommunityListItemResponse>

    // 내 피드 판매
    @GET("/refit/mypage/myfeed/sell")
    fun showMyFeedSell(
        @Field("postId") postId: Int,
        @Field("title") title: String,
        @Field("imgUrl") imgUrl: String,
        @Field("gender") gender: Int,
        @Field("deliveryType") deliveryType: Int,
        @Field("region") region: String,
        @Field("price") price: Int,
    ): Call<CommunityListItemResponse>

    // 내 피드 구매
    @GET("/refit/mypage/myfeed/buy")
    fun showMyFeedBuy(

    )

    // 내 스크랩 나눔
    @GET("/refit/mypage/scrap/give")
    fun showMyScrapGive(
        @Field("postId") postId: Int,
        @Field("title") title: String,
        @Field("imgUrl") imgUrl: String,
        @Field("gender") gender: Int,
        @Field("deliveryType") deliveryType: Int,
        @Field("region") region: String,
        @Field("price") price: Int,
    ): Call<CommunityListItemResponse>

    // 내 스크랩 구매
    @GET("/refit/mypage/myfeed/sell")
    fun showMyScrapSell(
        @Field("postId") postId: Int,
        @Field("title") title: String,
        @Field("imgUrl") imgUrl: String,
        @Field("gender") gender: Int,
        @Field("deliveryType") deliveryType: Int,
        @Field("region") region: String,
        @Field("price") price: Int,
    ): Call<CommunityListItemResponse>
}
/*
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = "Bearer $token"

        val requeset = chain.request().newBuilder()
            .addHeader("Authorization", token)
            .build()

        return chain.proceed(requeset)
    }
}*/
