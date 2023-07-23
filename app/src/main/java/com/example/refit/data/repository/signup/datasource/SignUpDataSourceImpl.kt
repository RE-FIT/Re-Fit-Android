package com.example.refit.data.repository.signup.datasource

import com.example.refit.data.model.signup.RequestEmailCertification
import com.example.refit.data.model.signup.ResponseEmailCertification
import com.example.refit.data.network.api.SignUpApi
import okhttp3.ResponseBody
import retrofit2.Call

class SignUpDataSourceImpl(private val signUpApi: SignUpApi) : SignUpDataSource {

    override suspend fun requestEmailCertification(body: RequestEmailCertification): Call<ResponseEmailCertification> {
        return signUpApi.requestEmailCertification(body)
    }

    override suspend fun requestLoginCertification(loginId: String, password: String): Call<ResponseBody> {
        return signUpApi.requestLoginCertification(loginId, password)
    }

    override suspend fun checkAccessToken(accessToken: String): Call<ResponseBody> {
        return signUpApi.checkAccessToken(accessToken)
    }

    override suspend fun logout(accessToken: String): Call<ResponseBody> {
        return signUpApi.logout(accessToken)
    }
}