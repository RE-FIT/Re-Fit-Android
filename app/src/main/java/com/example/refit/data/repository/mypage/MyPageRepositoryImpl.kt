package com.example.refit.data.repository.mypage

import com.example.refit.data.model.mypage.CheckNicknameResponse
import com.example.refit.data.repository.mypage.datasource.MyPageDataSource
import okhttp3.ResponseBody
import retrofit2.Call

class MyPageRepositoryImpl(private val myPageDataSource: MyPageDataSource): MyPageRepository {
    override suspend fun checkNickname(accessToken: String, name: String): Call<Boolean> {
        return myPageDataSource.checkNickname(accessToken, name)
    }

    override suspend fun loadCommunityList(token: String): Call<ResponseBody> {
        return myPageDataSource.loadCommunityList(token)
    }

    override suspend fun loadCommunityListSort(
        accesstoken: String,
        postType: Int,
        gender: Int,
        category: Int
    ): Call<ResponseBody> {
        return myPageDataSource.loadCommunityListSort(accesstoken, postType, gender, category)
    }

    override suspend fun showMyFeedSell(token: String): Call<ResponseBody> {
        return myPageDataSource.showMyFeedSell(token)
    }

}