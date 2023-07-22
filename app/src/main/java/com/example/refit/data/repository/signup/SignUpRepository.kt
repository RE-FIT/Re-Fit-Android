package com.example.refit.data.repository.signup

import com.example.refit.data.model.signup.RequestEmailCertification
import com.example.refit.data.model.signup.ResponseEmailCertification
import okhttp3.ResponseBody
import retrofit2.Call

interface SignUpRepository {
    suspend fun requestEmailCertification(body: RequestEmailCertification): Call<ResponseEmailCertification>

    suspend fun requestLoginCertification(loginId: String, password: String): Call<ResponseBody>

    suspend fun checkAccessToken(accessToken: String): Call<ResponseBody>

    suspend fun logout(accessToken: String): Call<ResponseBody>
}