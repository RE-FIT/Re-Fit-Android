package com.example.refit.data.repository.signup.datasource

import com.example.refit.data.model.signup.FindIdRequest
import com.example.refit.data.model.signup.FindIdResponse
import com.example.refit.data.model.signup.FindPasswordRequest
import com.example.refit.data.model.signup.RegisterUserRequest
import com.example.refit.data.model.signup.RequestEmailCertification
import com.example.refit.data.model.signup.ResponseEmailCertification
import okhttp3.ResponseBody
import retrofit2.Call

interface SignUpDataSource {
    suspend fun requestEmailCertification(body: RequestEmailCertification): Call<ResponseEmailCertification>

    suspend fun requestLoginCertification(loginId: String, password: String, fcm: String): Call<ResponseBody>

    suspend fun checkAccessToken(accessToken: String): Call<ResponseBody>

    suspend fun logout(accessToken: String): Call<ResponseBody>

    // 회원 가입용 임시 코드 (추후 삭제 예정)
    suspend fun requestJoinUser(body: RegisterUserRequest): Call<ResponseBody>

    //findById
    suspend fun findById(body: FindIdRequest): Call<FindIdResponse>

    //findByPasswprd
    suspend fun findByPassword(body: FindPasswordRequest): Call<ResponseBody>
}