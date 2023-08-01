package com.example.refit.data.repository.community

import androidx.lifecycle.LiveData
import com.example.refit.data.model.community.PostResponse
import com.example.refit.data.repository.community.datasource.CommunityDataSource
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.File

class CommunityRepositoryImpl (private val communityDataSource: CommunityDataSource): CommunityRepository {
    override suspend fun loadCommunityList(accessToken: String): Call<ResponseBody> {
        return communityDataSource.loadCommunityList(accessToken)
    }

    override suspend fun loadCommuintyListSort(
        accessToken: String,
        postType: Int,
        gender: Int,
        category: Int
    ): Call<ResponseBody> {
        return communityDataSource.loadCommunityListSort(accessToken, postType, gender, category)
    }

    override suspend fun createPostShareNDt(
        accessToken: String,
        title: String,
        gender: Int,
        postType: Int,
        category: Int,
        size: Int,
        deliveryType: Int,
        detail: String,
        sido: String,
        sigungu: String,
        bname: String,
        bname2: String,
        images: List<File>
    ): Call<ResponseBody> {
        return communityDataSource.createPostShareNDt(accessToken, title, gender, postType, category, size, deliveryType, detail, sido, sigungu, bname, bname2, images)
    }

    override suspend fun createPostShareNDelivery(
        accessToken: String,
        title: String,
        gender: Int,
        postType: Int,
        category: Int,
        size: Int,
        deliveryType: Int,
        deliveryFee: Int,
        detail: String,
        images: List<File>
    ): Call<ResponseBody> {
        return communityDataSource.createPostShareNDelivery(accessToken, title, gender, postType, category, size, deliveryType, deliveryFee, detail, images)
    }


    override suspend fun createPostSaleNDt(
        accessToken: String,
        title: String,
        gender: Int,
        postType: Int,
        price: Int,
        category: Int,
        size: Int,
        deliveryType: Int,
        detail: String,
        sido: String,
        sigungu: String,
        bname: String,
        bname2: String,
        images: List<File>
    ): Call<ResponseBody> {
        return communityDataSource.createPostSaleNDt(accessToken, title, gender, postType, price, category, size, deliveryType, detail, sido, sigungu, bname, bname2, images)
    }

    override suspend fun createPostSaleNDelivery(
        accessToken: String,
        title: String,
        gender: Int,
        postType: Int,
        price: Int,
        category: Int,
        size: Int,
        deliveryType: Int,
        deliveryFee: Int,
        detail: String,
        images: List<File>
    ): Call<ResponseBody> {
        return communityDataSource.createPostSaleNDelivery(accessToken, title, gender, postType, price, category, size, deliveryType, deliveryFee, detail, images)
    }

    override suspend fun getPost(accessToken: String, postInt: Int): Call<PostResponse> {
        return communityDataSource.getPost(accessToken, postInt)
    }

    override suspend fun loadSearchResult(
        accessToken: String,
        keyword: String
    ): Call<ResponseBody> {
        return communityDataSource.loadSearchResult(accessToken, keyword)
    }


}