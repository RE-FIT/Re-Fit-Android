package com.example.refit.data.repository.community

import androidx.lifecycle.LiveData
import com.example.refit.data.model.community.BlockDto
import com.example.refit.data.model.community.Member
import com.example.refit.data.model.community.PostResponse
import com.example.refit.data.model.community.ReportedUser
import com.example.refit.data.repository.community.datasource.CommunityDataSource
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    override suspend fun createPost(
        accessToken: String,
        postDto: RequestBody,
        image: List<File>
    ): Call<ResponseBody> {
        return communityDataSource.createPost(accessToken, postDto, image)
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

    override suspend fun modifyPostIncludeImage(
        accessToken: String,
        image_updated: Boolean,
        postId: Int,
        postDto: RequestBody,
        image: List<File>
    ): Call<ResponseBody> {
        return communityDataSource.modifyPostIncludeImage(accessToken, image_updated, postId, postDto, image)
    }

    override suspend fun modifyPost(
        accessToken: String,
        image_updated: Boolean,
        postId: Int,
        postDto: RequestBody
    ): Call<ResponseBody> {
        return communityDataSource.modifyPost(accessToken, image_updated, postId, postDto)
    }

    override suspend fun deletePost(accessToken: String, postId: Int): Call<ResponseBody> {
        return communityDataSource.deletePost(accessToken, postId)
    }

    override suspend fun scrapPost(accessToken: String, postId: Int): Call<ResponseBody> {
        return communityDataSource.scrapPost(accessToken, postId)
    }

    override suspend fun blockUser(
        accessToken: String,
        blockDto: BlockDto
    ): Call<ResponseBody> {
        return communityDataSource.blockUser(accessToken, blockDto)
    }

    override suspend fun reportUser(
        accessToken: String,
        reportedUser: ReportedUser
    ): Call<ResponseBody> {
        return communityDataSource.reportUser(accessToken, reportedUser)
    }

    override suspend fun changePostStatus(accessToken: String, postId: Int): Call<PostResponse> {
        return communityDataSource.changePostStatus(accessToken, postId)
    }


}