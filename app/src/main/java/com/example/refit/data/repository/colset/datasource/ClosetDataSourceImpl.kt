package com.example.refit.data.repository.colset.datasource

import com.example.refit.data.model.closet.RequestRegisteredClothes
import com.example.refit.data.model.closet.ResponseAddNewCloth
import com.example.refit.data.model.closet.ResponseRegisteredClothes
import com.example.refit.data.network.api.ClosetApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call

class ClosetDataSourceImpl(private val closetApi: ClosetApi): ClosetDataSource {

    override suspend fun addNewCloth(
        token: String,
        image: MultipartBody.Part,
        request: RequestBody
    ): Call<Long> {
        return closetApi.addNewCloth(token, image, request)
    }

    override suspend fun getRegisteredClothes(
        token: String,
        request: RequestRegisteredClothes
    ): Call<List<ResponseRegisteredClothes>> {
        return closetApi.getRegisteredClothes(
            token,
            request.category,
            request.season,
            request.sort
        )
    }
}