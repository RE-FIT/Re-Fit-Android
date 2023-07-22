package com.example.refit.data.repository.signup

import com.example.refit.data.model.signup.RequestEmailCertification
import com.example.refit.data.model.signup.ResponseEmailCertification
import com.example.refit.data.repository.signup.datasource.SignUpDataSource
import okhttp3.ResponseBody
import retrofit2.Call

class SignUpRepositoryImpl(private val signUpDataSource: SignUpDataSource) : SignUpRepository {

    override suspend fun requestEmailCertification(body: RequestEmailCertification): Call<ResponseEmailCertification> {
        return signUpDataSource.requestEmailCertification(body)
    }

    override suspend fun requestLoginCertification(loginId: String, password: String): Call<ResponseBody> {
        return signUpDataSource.requestLoginCertification(loginId, password)
    }

    override suspend fun checkAccessToken(accessToken: String): Call<ResponseBody> {
        return signUpDataSource.checkAccessToken(accessToken)
    }

    override suspend fun logout(accessToken: String): Call<ResponseBody> {
        return signUpDataSource.logout(accessToken)
    }
}