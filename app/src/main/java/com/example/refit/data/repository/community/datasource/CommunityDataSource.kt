package com.example.refit.data.repository.community.datasource

import okhttp3.ResponseBody
import retrofit2.Call

interface CommunityDataSource {
    suspend fun loadCommunityList(accessToken: String, postType: Int, gender: Int, category: Int, region: String): Call<ResponseBody>
}