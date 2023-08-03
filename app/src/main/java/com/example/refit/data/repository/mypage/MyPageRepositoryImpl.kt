package com.example.refit.data.repository.mypage

import com.example.refit.data.model.mypage.CheckNicknameResponse
import com.example.refit.data.repository.mypage.datasource.MyPageDataSource
import okhttp3.ResponseBody
import retrofit2.Call

class MyPageRepositoryImpl(private val myPageDataSource: MyPageDataSource): MyPageRepository {
    override suspend fun showMyInfo(token: String, name: String): Call<CheckNicknameResponse> {
        return myPageDataSource.showMyInfo(token, name)
    }
}