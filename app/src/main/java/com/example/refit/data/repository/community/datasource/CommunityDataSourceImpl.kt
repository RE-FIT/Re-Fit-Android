package com.example.refit.data.repository.community.datasource

import com.example.refit.data.model.community.BlockDto
import com.example.refit.data.model.community.Member
import com.example.refit.data.model.community.PostResponse
import com.example.refit.data.model.community.ReportedUser
import com.example.refit.data.network.api.CommunityApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.File

class CommunityDataSourceImpl(private val communityApi: CommunityApi) : CommunityDataSource {

    override suspend fun loadCommunityList(accessToken: String): Call<ResponseBody> {
        return communityApi.loadCommunityList(accessToken)
    }

    override suspend fun loadCommunityListSort(
        accessToken: String,
        postType: Int,
        gender: Int,
        category: Int
    ): Call<ResponseBody> {
        return communityApi.loadCommunityList(accessToken, postType, gender, category)
    }

    override suspend fun createPost(
        accessToken: String,
        postDto: RequestBody,
        image: List<File>
    ): Call<ResponseBody> {

        val imageParts: List<MultipartBody.Part> = image.map { file ->
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", file.name, requestFile)
        }

        return communityApi.createPost(accessToken, postDto, imageParts)
    }

    override suspend fun getPost(accessToken: String, postId: Int): Call<PostResponse> {
        return communityApi.getPost(accessToken, postId)
    }

    override suspend fun loadSearchResult(
        accessToken: String,
        keyword: String
    ): Call<ResponseBody> {
        return communityApi.loadSearchResult(accessToken, keyword)
    }

    override suspend fun deletePost(accessToken: String, postId: Int): Call<ResponseBody> {
        return communityApi.deletePost(accessToken, postId)
    }

    override suspend fun changePostStatus(accessToken: String, postId: Int): Call<PostResponse> {
        return communityApi.changePostStatus(accessToken, postId)
    }

    override suspend fun modifyPostIncludeImage(
        accessToken: String,
        image_updated: Boolean,
        postId: Int,
        postDto: RequestBody,
        image: List<File>
    ): Call<ResponseBody> {

        val imageParts: List<MultipartBody.Part> = image.map { file ->
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", file.name, requestFile)
        }

        return communityApi.modifyPostIncludeImage(accessToken, postId, postDto, image_updated, imageParts)
    }

    override suspend fun modifyPost(
        accessToken: String,
        image_updated: Boolean,
        postId: Int,
        postDto: RequestBody
    ): Call<ResponseBody> {
        return communityApi.modifyPost(accessToken, postId, postDto, image_updated)
    }

    override suspend fun scrapPost(accessToken: String, postId: Int): Call<ResponseBody> {
        return communityApi.scrapPost(accessToken, postId)
    }

    override suspend fun blockUser(
        accessToken: String,
        blockDto: BlockDto
    ): Call<ResponseBody> {
        return communityApi.blockUser(accessToken, blockDto)
    }

    override suspend fun reportUser(
        accessToken: String,
        reportedUser: ReportedUser
    ): Call<ResponseBody> {
        return communityApi.reportUser(accessToken, reportedUser)
    }

}