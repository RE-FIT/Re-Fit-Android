package com.example.refit.data.repository.signup.datasource

import com.example.refit.data.model.signup.FindIdRequest
import com.example.refit.data.model.signup.FindIdResponse
import com.example.refit.data.model.signup.FindPasswordRequest
import com.example.refit.data.model.signup.RegisterUserRequest
import com.example.refit.data.model.signup.RequestEmailCertification
import com.example.refit.data.model.signup.ResponseEmailCertification
import com.example.refit.data.network.api.SignUpApi
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class SignUpDataSourceImpl(private val signUpApi: SignUpApi) : SignUpDataSource {

    override suspend fun requestEmailCertification(body: RequestEmailCertification): Call<ResponseEmailCertification> {
        return signUpApi.requestEmailCertification(body)
    }

    override suspend fun requestLoginCertification(loginId: String, password: String, fcm: String): Call<ResponseBody> {
        return signUpApi.requestLoginCertification(loginId, password, fcm)
    }

    override suspend fun checkAccessToken(accessToken: String): Call<ResponseBody> {
        return signUpApi.checkAccessToken(accessToken)
    }

    override suspend fun logout(accessToken: String): Call<ResponseBody> {
        return signUpApi.logout(accessToken)
    }

    // 회원 가입용 임시 코드 (추후 삭제 예정)
    override suspend fun requestJoinUser(body: RegisterUserRequest): Call<ResponseBody> {
        return signUpApi.requestJoinUser(body)
    }

    override suspend fun findById(body: FindIdRequest): Call<FindIdResponse> {
        return signUpApi.findId(body)
    }

    override suspend fun findByPassword(body: FindPasswordRequest): Call<ResponseBody> {
        return signUpApi.findPassword(body)
    }
}