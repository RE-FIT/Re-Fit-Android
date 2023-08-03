package com.example.refit.data.repository.community

import androidx.lifecycle.LiveData
import com.example.refit.data.model.community.PostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.File

interface CommunityRepository {
    suspend fun loadCommunityList(accessToken: String): Call<ResponseBody>

    suspend fun loadCommuintyListSort(accessToken: String, postType: Int, gender: Int, category: Int): Call<ResponseBody>

    suspend fun createPost(
        accessToken: String,
        postDto: RequestBody,
        image: List<File>
    ): Call<ResponseBody>

    suspend fun getPost(accessToken: String, postInt: Int): Call<PostResponse>
    suspend fun loadSearchResult(accessToken: String, keyword: String): Call<ResponseBody>

}