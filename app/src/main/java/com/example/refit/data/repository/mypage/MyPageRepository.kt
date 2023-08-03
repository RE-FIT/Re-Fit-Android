package com.example.refit.data.repository.mypage

import com.example.refit.data.model.mypage.CheckNicknameResponse
import okhttp3.ResponseBody
import retrofit2.Call

interface MyPageRepository {
    suspend fun showMyInfo(token: String, name: String): Call<CheckNicknameResponse>
}