package com.example.refit.data.repository.signup.datasource

import com.example.refit.data.model.signup.RequestEmailCertification
import com.example.refit.data.model.signup.ResponseEmailCertification
import com.example.refit.data.network.api.SignUpApi
import retrofit2.Call

class SignUpDataSourceImpl(private val signUpApi: SignUpApi) : SignUpDataSource {

    override suspend fun requestEmailCertification(body: RequestEmailCertification): Call<ResponseEmailCertification> {
        return signUpApi.requestEmailCertification(body)
    }
}