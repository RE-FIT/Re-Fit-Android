package com.example.refit.data.repository.mypage.datasource

import com.example.refit.data.model.mypage.CheckNicknameResponse
import okhttp3.ResponseBody
import retrofit2.Call

interface MyPageDataSource {
    suspend fun showMyInfo(token: String, name: String): Call<CheckNicknameResponse>
}