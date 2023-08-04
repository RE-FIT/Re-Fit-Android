package com.example.refit.data.repository.mypage.datasource

import com.example.refit.data.model.community.PostResponse
import com.example.refit.data.model.mypage.CheckNicknameResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.File

interface MyPageDataSource {
    suspend fun checkNickname(token: String, name: String): Call<CheckNicknameResponse>

    suspend fun loadCommunityList(accessToken: String): Call<ResponseBody>

    suspend fun loadCommunityListSort(
        token: String,
        postType: Int,
        gender: Int,
        category: Int
    ): Call<ResponseBody>

    suspend fun showMyFeedSell(token: String): Call<ResponseBody>

}