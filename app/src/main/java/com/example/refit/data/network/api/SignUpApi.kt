package com.example.refit.data.network.api

import com.example.refit.data.model.signup.RegisterUserRequest
import com.example.refit.data.model.signup.RequestEmailCertification
import com.example.refit.data.model.signup.ResponseEmailCertification
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface SignUpApi {

    @POST("auth/email")
    fun requestEmailCertification(
        @Body email: RequestEmailCertification
    ): Call<ResponseEmailCertification>

    @POST("/auth/login")
    fun requestLoginCertification(
        @Query("loginId") loginId: String,
        @Query("password") password: String
    ): Call<ResponseBody>

    @GET("auth/token/check")
    fun checkAccessToken(
        @Header("Authorization") accessToken: String
    ): Call<ResponseBody>


    @POST("auth/logout")
    fun logout(
        @Header("Authorization") accessToken: String
    ): Call<ResponseBody>

    // 테스트용 임시 회원가입 코드 (추후 삭제)
    @POST("auth/join")
    fun requestJoinUser(
        @Body requestBody: RegisterUserRequest
    ): Call<ResponseBody>
}