package com.example.refit.data.repository.mypage.datasource

import com.example.refit.data.model.community.CommunityListItemResponse
import com.example.refit.data.model.community.PostResponse
import com.example.refit.data.model.mypage.CheckNicknameResponse
import com.example.refit.data.model.mypage.MyFeedBuyListItemResponse
import com.example.refit.data.model.mypage.MyFeedGiveListItemResponse
import com.example.refit.data.model.mypage.MyFeedSellListItemResponse
import com.example.refit.data.model.mypage.MyScrapGiveListItemResponse
import com.example.refit.data.model.mypage.MyScrapSellListItemResponse
import com.example.refit.data.model.mypage.ShowMyInfoResponse
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

    override suspend fun showMyInfo(accessToken: String): Call<ShowMyInfoResponse> {
        return myPageApi.showMyInfo(accessToken)
    }

    override suspend fun checkNickname(accessToken: String, name: String): Call<Boolean> {
        return myPageApi.checkNickname(accessToken, name)
    }

    override suspend fun updatePassword(accessToken: String, currentPw: String, newPw: String): Call<ResponseBody> {
        return myPageApi.updatePassword(accessToken, currentPw, newPw)
    }
    override suspend fun loadCommunityListSort(
        token: String,
        postType: Int,
        gender: Int,
        category: Int
    ): Call<ResponseBody> {
        return communityApi.loadCommunityList(token, postType, gender, category)
    }
    override suspend fun loadMyFeedBuyList(accessToken: String): Call<List<MyFeedBuyListItemResponse>> {
        return myPageApi.showMyFeedBuy(accessToken)
    }
    override suspend fun loadMyFeedSellList(accessToken: String): Call<List<MyFeedSellListItemResponse>> {
        return myPageApi.showMyFeedSell(accessToken)
    }

    override suspend fun loadMyFeedGiveList(accessToken: String): Call<List<MyFeedGiveListItemResponse>> {
        return myPageApi.showMyFeedGive(accessToken)
    }
    override suspend fun loadMyScrapSellList(accessToken: String): Call<List<MyScrapSellListItemResponse>> {
        return myPageApi.showMyScrapSell(accessToken)
    }
    override suspend fun loadMyScrapGiveList(accessToken: String): Call<List<MyScrapGiveListItemResponse>> {
        return myPageApi.showMyScrapGive(accessToken)
    }


}