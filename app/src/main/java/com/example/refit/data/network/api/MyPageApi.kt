package com.example.refit.data.network.api

import com.example.refit.data.model.mypage.MyFeedBuyListItemResponse
import com.example.refit.data.model.mypage.MyFeedGiveListItemResponse
import com.example.refit.data.model.mypage.MyFeedSellListItemResponse
import com.example.refit.data.model.mypage.MyScrapGiveListItemResponse
import com.example.refit.data.model.mypage.MyScrapSellListItemResponse
import com.example.refit.data.model.mypage.PasswordUpdateRequest
import com.example.refit.data.model.mypage.ShowMyInfoResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part
import retrofit2.http.Query

interface MyPageApi {
    // 회원 정보 조회
    @GET("/refit/mypage/info")
    fun showMyInfo(
        @Header("Authorization") accessToken: String
    ): Call<ShowMyInfoResponse>

    // 회원 정보 수정
    @Multipart
    @PATCH("/refit/mypage/info")
    fun updateInfo(
        @Header("Authorization") accessToken: String,
        @Part("image") image: List<Unit>,
        @Part("content") content: RequestBody?,
    ) : Call<Response<Void>>

    @Multipart
    @PATCH("/refit/mypage/info")
    fun updateInfoNoImage(
        @Header("Authorization") accessToken: String,
        @Part("content") content: RequestBody?,
    ) : Call<Response<Void>>

    // 이름(닉네임) 중복 확인
    @GET("/refit/mypage/info/check")
    fun checkNickname(
        @Header("Authorization") accessToken: String,
        @Query("name") name: String
    ): Call<Boolean>

    @PATCH("/refit/mypage/info/password")
    fun updatePassword(
        @Header("Authorization") accessToken: String,
        @Body request: PasswordUpdateRequest
    ): Call<Response<Void>>

    // 내 피드 나눔
    @GET("/refit/mypage/myfeed/give")
    fun showMyFeedGive(
        @Header("Authorization") accessToken: String
    ): Call<List<MyFeedGiveListItemResponse>>

    // 내 피드 판매
    @GET("/refit/mypage/myfeed/sell")
    fun showMyFeedSell(
        @Header("Authorization") token: String
    ): Call<List<MyFeedSellListItemResponse>>

    // 내 피드 구매
    @GET("/refit/mypage/myfeed/buy")
    fun showMyFeedBuy(
        @Header("Authorization") token: String
    ): Call<List<MyFeedBuyListItemResponse>>

    // 내 스크랩 나눔
    @GET("/refit/mypage/scrap/give")
    fun showMyScrapGive(
        @Header("Authorization") token: String
    ): Call<List<MyScrapGiveListItemResponse>>

    // 내 스크랩 판매
    @GET("/refit/mypage/scrap/sell")
    fun showMyScrapSell(
        @Header("Authorization") token: String
    ): Call<List<MyScrapSellListItemResponse>>
}
