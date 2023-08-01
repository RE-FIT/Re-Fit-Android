package com.example.refit.data.repository.community

import androidx.lifecycle.LiveData
import com.example.refit.data.model.community.PostResponse
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.File

interface CommunityRepository {
    suspend fun loadCommunityList(accessToken: String): Call<ResponseBody>

    suspend fun loadCommuintyListSort(accessToken: String, postType: Int, gender: Int, category: Int): Call<ResponseBody>

    // 나눔 && 직거래
    suspend fun createPostShareNDt(
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
    ): Call<ResponseBody>

    // 나눔 && 배송
    suspend fun createPostShareNDelivery(
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
    ): Call<ResponseBody>

    // 판매 && 직거래
    suspend fun createPostSaleNDt(
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
    ): Call<ResponseBody>

    // 판매 && 배송
    suspend fun createPostSaleNDelivery(
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
    ): Call<ResponseBody>

    suspend fun getPost(accessToken: String, postInt: Int): Call<PostResponse>
    suspend fun loadSearchResult(accessToken: String, keyword: String): Call<ResponseBody>

}