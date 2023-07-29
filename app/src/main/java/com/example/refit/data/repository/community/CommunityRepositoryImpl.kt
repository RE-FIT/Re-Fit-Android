package com.example.refit.data.repository.community

import com.example.refit.data.repository.community.datasource.CommunityDataSource
import okhttp3.ResponseBody
import retrofit2.Call

class CommunityRepositoryImpl (private val communityDataSource: CommunityDataSource): CommunityRepository {
    override suspend fun loadCommunityList(accessToken: String, postType: Int, gender: Int, category: Int, region: String): Call<ResponseBody> {
        return communityDataSource.loadCommunityList(accessToken, postType, gender, category, region)
    }

}