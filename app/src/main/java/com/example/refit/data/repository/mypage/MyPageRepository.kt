package com.example.refit.data.repository.mypage

import com.example.refit.data.model.mypage.MyFeedBuyListItemResponse
import com.example.refit.data.model.mypage.MyFeedGiveListItemResponse
import com.example.refit.data.model.mypage.MyFeedSellListItemResponse
import com.example.refit.data.model.mypage.MyInfoResponse
import com.example.refit.data.model.mypage.MyScrapGiveListItemResponse
import com.example.refit.data.model.mypage.MyScrapSellListItemResponse
import com.example.refit.data.model.mypage.PasswordUpdateRequest
import com.example.refit.data.model.mypage.ShowMyInfoResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

interface MyPageRepository {
    suspend fun myInfo(accessToken: String): Call<MyInfoResponse>
    suspend fun checkNickname(accessToken: String, name: String): Call<Boolean>
    suspend fun showMyInfo(accessToken: String): Call<ShowMyInfoResponse>
    suspend fun updatePassword(accessToken: String, request: PasswordUpdateRequest): Call<Response<Void>>
    suspend fun loadCommunityListSort(
        token: String,
        postType: Int,
        gender: Int,
        category: Int
    ): Call<ResponseBody>
    suspend fun loadMyFeedBuyList(accessToken: String): Call<List<MyFeedBuyListItemResponse>>
    suspend fun loadMyFeedSellList(accessToken: String): Call<List<MyFeedSellListItemResponse>>
    suspend fun loadMyFeedGiveList(accessToken: String): Call<List<MyFeedGiveListItemResponse>>
    suspend fun loadMyScrapSellList(accessToken: String): Call<List<MyScrapSellListItemResponse>>
    suspend fun loadMyScrapGiveList(accessToken: String): Call<List<MyScrapGiveListItemResponse>>
}