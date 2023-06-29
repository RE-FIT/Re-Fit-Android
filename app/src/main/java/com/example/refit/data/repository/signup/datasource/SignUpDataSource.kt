package com.example.refit.data.repository.signup.datasource

import com.example.refit.data.model.signup.RequestEmailCertification
import com.example.refit.data.model.signup.ResponseEmailCertification
import retrofit2.Call

interface SignUpDataSource {
    suspend fun requestEmailCertification(body: RequestEmailCertification): Call<ResponseEmailCertification>
}