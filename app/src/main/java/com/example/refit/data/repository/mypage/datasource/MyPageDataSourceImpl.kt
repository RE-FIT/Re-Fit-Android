package com.example.refit.data.repository.mypage.datasource

import com.example.refit.data.model.mypage.CheckNicknameResponse
import com.example.refit.data.network.api.MyPageApi
import okhttp3.ResponseBody
import retrofit2.Call

class MyPageDataSourceImpl (private val myPageApi: MyPageApi): MyPageDataSource {

    override suspend fun showMyInfo(token: String, name: String): Call<CheckNicknameResponse> {
        return myPageApi.showMyInfo(token, name)
    }
}