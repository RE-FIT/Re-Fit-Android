package com.example.refit.data.network.api

import com.example.refit.data.model.signup.RequestEmailCertification
import com.example.refit.data.model.signup.ResponseEmailCertification
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpApi {

    @POST("auth/email")
    fun requestEmailCertification(
        @Body email: RequestEmailCertification
    ): Call<ResponseEmailCertification>
}