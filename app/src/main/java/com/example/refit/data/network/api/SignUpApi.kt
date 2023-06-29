package com.example.refit.data.network.api

import com.example.refit.data.model.signup.RequestEmailCertification
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpApi {

    @POST("auth/email")
    suspend fun requestEmailCertification(
        @Body email: RequestEmailCertification
    )
}