package com.example.refit.data.repository.mypage.datasource

import com.example.refit.data.model.community.PostResponse
import com.example.refit.data.model.mypage.CheckNicknameResponse
import com.example.refit.data.network.api.CommunityApi
import com.example.refit.data.network.api.MyPageApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.File

class MyPageDataSourceImpl (private val myPageApi: MyPageApi, private val communityApi: CommunityApi): MyPageDataSource {
    override suspend fun loadCommunityList(token: String): Call<ResponseBody> {
        return myPageApi.showMyFeedBuy(token)
    }

    override suspend fun checkNickname(token: String, name: String): Call<CheckNicknameResponse> {
        return myPageApi.checkNickname(token, name)
    }

    override suspend fun loadCommunityListSort(
        token: String,
        postType: Int,
        gender: Int,
        category: Int
    ): Call<ResponseBody> {
        return communityApi.loadCommunityList(token, postType, gender, category)
    }

    override suspend fun showMyFeedSell(token: String): Call<ResponseBody> {
        return myPageApi.showMyFeedSell(token)
    }

}