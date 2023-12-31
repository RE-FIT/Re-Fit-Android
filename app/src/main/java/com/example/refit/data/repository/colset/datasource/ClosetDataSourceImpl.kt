package com.example.refit.data.repository.colset.datasource

import com.example.refit.data.model.closet.RequestAddNewCloth
import com.example.refit.data.model.closet.RequestRegisteredClothes
import com.example.refit.data.model.closet.RequestResetCompletedCloth
import com.example.refit.data.model.closet.ResponseAddNewCloth
import com.example.refit.data.model.closet.ResponseForestStatusInfo
import com.example.refit.data.model.closet.ResponseQuizInfo
import com.example.refit.data.model.closet.ResponseRegisteredClothInfo
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
    override suspend fun deleteClothItem(token: String, clothId: Int): Call<Void> {
        return closetApi.deleteClothItem(token, clothId)
    }

    override suspend fun getRegisteredClothInfo(
        token: String,
        clothId: Int
    ): Call<ResponseRegisteredClothInfo> {
        return closetApi.getRegisteredClothInfo(token, clothId)
    }

    override suspend fun fixClothItem(
        token: String,
        request: RequestAddNewCloth,
        clothId: Int
    ): Call<Void> {
        return closetApi.fixClothItem(token, clothId, request)
    }

    override suspend fun resetCompletedCloth(
        token: String,
        request: RequestResetCompletedCloth,
        clothId: Int
    ): Call<Void> {
        return closetApi.resetCompletedCloth(token, clothId, request)
    }

    override suspend fun wearClothes(token: String, id: Int): Call<Void> {
        return closetApi.wearClothes(token, id)
    }

    override suspend fun getForestStatusInfo(
        token: String,
        id: Int
    ): Call<ResponseForestStatusInfo> {
        return closetApi.getForestStatusInfo(token, id)
    }

    override suspend fun getQuizInfo(token: String, id: Int): Call<ResponseQuizInfo> {
        return closetApi.getQuizInfo(token, id)
    }
}