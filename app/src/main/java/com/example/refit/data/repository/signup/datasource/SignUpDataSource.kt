package com.example.refit.data.repository.signup.datasource

import com.example.refit.data.model.signup.RequestEmailCertification
import com.example.refit.data.model.signup.ResponseEmailCertification
import okhttp3.ResponseBody
import retrofit2.Call

interface SignUpDataSource {
    suspend fun requestEmailCertification(body: RequestEmailCertification): Call<ResponseEmailCertification>

    suspend fun requestLoginCertification(loginId: String, password: String): Call<ResponseBody>

    suspend fun checkAccessToken(accessToken: String): Call<ResponseBody>

    suspend fun logout(accessToken: String): Call<ResponseBody>
}