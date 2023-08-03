package com.example.refit.data.repository.colset.datasource

import com.example.refit.data.model.closet.ResponseAddNewCloth
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call

interface ClosetDataSource {

    suspend fun addNewCloth(
        token: String,
        image: MultipartBody.Part,
        request: RequestBody
    ): Call<ResponseAddNewCloth>

}