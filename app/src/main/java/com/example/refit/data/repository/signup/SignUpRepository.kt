package com.example.refit.data.repository.signup

import com.example.refit.data.model.signup.RequestEmailCertification
import com.example.refit.data.model.signup.ResponseEmailCertification
import retrofit2.Call

interface SignUpRepository {
    suspend fun requestEmailCertification(body: RequestEmailCertification): Call<ResponseEmailCertification>
}