package com.example.refit.data.repository.signup

import com.example.refit.data.model.signup.FindIdRequest
import com.example.refit.data.model.signup.FindIdResponse
import com.example.refit.data.model.signup.FindPasswordRequest
import com.example.refit.data.model.signup.RegisterUserRequest
import com.example.refit.data.model.signup.RequestEmailCertification
import com.example.refit.data.model.signup.RequestNicknameValidation
import com.example.refit.data.model.signup.ResponseEmailCertification
import com.example.refit.data.repository.signup.datasource.SignUpDataSource
import okhttp3.ResponseBody
import retrofit2.Call
import kotlin.math.sign

class SignUpRepositoryImpl(private val signUpDataSource: SignUpDataSource) : SignUpRepository {

    override suspend fun requestEmailCertification(body: RequestEmailCertification): Call<ResponseEmailCertification> {
        return signUpDataSource.requestEmailCertification(body)
    }

    override suspend fun requestLoginCertification(loginId: String, password: String, fcm: String): Call<ResponseBody> {
        return signUpDataSource.requestLoginCertification(loginId, password, fcm)
    }

    override suspend fun checkAccessToken(accessToken: String): Call<ResponseBody> {
        return signUpDataSource.checkAccessToken(accessToken)
    }

    override suspend fun logout(accessToken: String): Call<ResponseBody> {
        return signUpDataSource.logout(accessToken)
    }

    // 회원 가입용 임시 코드 (추후 삭제 예정)
    override suspend fun requestJoinUser(body: RegisterUserRequest): Call<ResponseBody> {
        return signUpDataSource.requestJoinUser(body)
    }

    override suspend fun findById(body: FindIdRequest): Call<FindIdResponse> {
        return signUpDataSource.findById(body)
    }

    override suspend fun findByPassword(body: FindPasswordRequest): Call<ResponseBody> {
        return signUpDataSource.findByPassword(body)
    }

    override suspend fun checkNicknameValidation(body: RequestNicknameValidation): Call<Void> {
        return signUpDataSource.checkNicknameValidation(body)
    }
}