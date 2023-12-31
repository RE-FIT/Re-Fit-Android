package com.example.refit.data.repository.mypage

import com.example.refit.data.model.mypage.MyFeedBuyListItemResponse
import com.example.refit.data.model.mypage.MyFeedGiveListItemResponse
import com.example.refit.data.model.mypage.MyFeedSellListItemResponse
import com.example.refit.data.model.mypage.MyInfoResponse
import com.example.refit.data.model.mypage.MyScrapGiveListItemResponse
import com.example.refit.data.model.mypage.MyScrapSellListItemResponse
import com.example.refit.data.model.mypage.PasswordUpdateRequest
import com.example.refit.data.model.mypage.ShowMyInfoResponse
import com.example.refit.data.model.mypage.UpdateDTO
import com.example.refit.data.repository.mypage.datasource.MyPageDataSource
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

class MyPageRepositoryImpl(private val myPageDataSource: MyPageDataSource): MyPageRepository {
    override suspend fun myInfo(accessToken: String): Call<MyInfoResponse> {
        return myPageDataSource.myInfo(accessToken)
    }

    override suspend fun checkNickname(accessToken: String, name: String): Call<Boolean> {
        return myPageDataSource.checkNickname(accessToken, name)
    }
    override suspend fun showMyInfo(accessToken: String): Call<ShowMyInfoResponse> {
        return myPageDataSource.showMyInfo(accessToken)
    }
    override suspend fun updatePassword(accessToken: String, request: PasswordUpdateRequest): Call<Response<Void>> {
        return myPageDataSource.updatePassword(accessToken, request)
    }
    override suspend fun updateInfoNoImage(accessToken: String, content: RequestBody): Call<Response<Void>> {
        return myPageDataSource.updateInfoNoImage(accessToken, content)
    }
    override suspend fun updateInfo(
        accessToken: String,
        image: File?,
        request: UpdateDTO
    ): Call<ResponseBody> {
        return myPageDataSource.updateInfo(accessToken, image, request)
    }
    override suspend fun loadCommunityListSort(
        token: String,
        postType: Int,
        gender: Int,
        category: Int
    ): Call<ResponseBody> {
        return myPageDataSource.loadCommunityListSort(token, postType, gender, category)
    }
    override suspend fun loadMyFeedBuyList(accessToken: String): Call<List<MyFeedBuyListItemResponse>> {
        return myPageDataSource.loadMyFeedBuyList(accessToken)
    }
    override suspend fun loadMyFeedSellList(accessToken: String): Call<List<MyFeedSellListItemResponse>> {
        return myPageDataSource.loadMyFeedSellList(accessToken)
    }

    override suspend fun loadMyFeedGiveList(accessToken: String): Call<List<MyFeedGiveListItemResponse>> {
        return myPageDataSource.loadMyFeedGiveList(accessToken)
    }

    override suspend fun loadMyScrapSellList(accessToken: String): Call<List<MyScrapSellListItemResponse>> {
        return myPageDataSource.loadMyScrapSellList(accessToken)
    }

    override suspend fun loadMyScrapGiveList(accessToken: String): Call<List<MyScrapGiveListItemResponse>> {
        return myPageDataSource.loadMyScrapGiveList(accessToken)
    }

}