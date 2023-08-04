package com.example.refit.data.repository.mypage

import com.example.refit.data.model.mypage.CheckNicknameResponse
import okhttp3.ResponseBody
import retrofit2.Call

interface MyPageRepository {
    suspend fun checkNickname(token: String, name: String): Call<CheckNicknameResponse>
    suspend fun loadCommunityList(token: String): Call<ResponseBody>
    suspend fun loadCommunityListSort(accessToken: String, postType: Int, gender: Int, category: Int): Call<ResponseBody>
    suspend fun showMyFeedSell(token: String): Call<ResponseBody>

}