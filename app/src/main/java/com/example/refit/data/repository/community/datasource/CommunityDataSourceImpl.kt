package com.example.refit.data.repository.community.datasource

import com.example.refit.data.network.api.CommunityApi
import okhttp3.ResponseBody
import retrofit2.Call

class CommunityDataSourceImpl (private val communityApi: CommunityApi): CommunityDataSource {
    override suspend fun loadCommunityList(
        accessToken: String,
        postType: Int,
        gender: Int,
        category: Int,
        region: String
    ): Call<ResponseBody> {
        TODO("Not yet implemented")
    }
}