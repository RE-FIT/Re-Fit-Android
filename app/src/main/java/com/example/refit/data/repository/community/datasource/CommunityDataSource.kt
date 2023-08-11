package com.example.refit.data.repository.community.datasource

import com.example.refit.data.model.community.BlockDto
import com.example.refit.data.model.community.Member
import com.example.refit.data.model.community.PostResponse
import com.example.refit.data.model.community.ReportedUser
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.File

interface CommunityDataSource {
    suspend fun loadCommunityList(accessToken: String): Call<ResponseBody>

    suspend fun loadCommunityListSort(
        accessToken: String,
        postType: Int,
        gender: Int,
        category: Int,
    ): Call<ResponseBody>

    suspend fun createPost(
        accessToken: String,
        postDto: RequestBody,
        image: List<File>
    ): Call<ResponseBody>

    suspend fun getPost(accessToken: String, postId: Int): Call<PostResponse>
    suspend fun loadSearchResult(accessToken: String, keyword: String): Call<ResponseBody>

    suspend fun deletePost(accessToken: String, postId: Int): Call<ResponseBody>

    suspend fun changePostStatus(accessToken: String, postId: Int): Call<PostResponse>

    suspend fun modifyPostIncludeImage(
        accessToken: String,
        image_updated: Boolean,
        postId: Int,
        postDto: RequestBody,
        image: List<File>
    ): Call<ResponseBody>

    suspend fun modifyPost(
        accessToken: String,
        image_updated: Boolean,
        postId: Int,
        postDto: RequestBody,
    ): Call<ResponseBody>

    suspend fun scrapPost(accessToken: String, postId: Int): Call<ResponseBody>

    suspend fun blockUser(accessToken: String, blockDto: BlockDto): Call<ResponseBody>

    suspend fun reportUser(accessToken: String, reportedUser: ReportedUser): Call<ResponseBody>

}