package com.example.refit.data.repository.signup

import com.example.refit.data.model.signup.RequestEmailCertification
import com.example.refit.data.model.signup.ResponseEmailCertification
import com.example.refit.data.repository.signup.datasource.SignUpDataSource
import retrofit2.Call

class SignUpRepositoryImpl(private val signUpDataSource: SignUpDataSource) : SignUpRepository {

    override suspend fun requestEmailCertification(body: RequestEmailCertification): Call<ResponseEmailCertification> {
        return signUpDataSource.requestEmailCertification(body)
    }
}