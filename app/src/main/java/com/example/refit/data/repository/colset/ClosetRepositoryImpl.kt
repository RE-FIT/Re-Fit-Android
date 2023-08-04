package com.example.refit.data.repository.colset

import com.example.refit.data.model.closet.ResponseAddNewCloth
import com.example.refit.data.repository.colset.datasource.ClosetDataSource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call

class ClosetRepositoryImpl(private val closetDataSource: ClosetDataSource): ClosetRepository {

    override suspend fun addNewCloth(
        token: String,
        image: MultipartBody.Part,
        request: RequestBody
    ): Call<Long> {
        return closetDataSource.addNewCloth(token, image, request)
    }
}