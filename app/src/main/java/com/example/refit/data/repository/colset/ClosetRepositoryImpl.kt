package com.example.refit.data.repository.colset

import com.example.refit.data.model.closet.RequestRegisteredClothes
import com.example.refit.data.model.closet.ResponseAddNewCloth
import com.example.refit.data.model.closet.ResponseRegisteredClothInfo
import com.example.refit.data.model.closet.ResponseRegisteredClothes
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

    override suspend fun getRegisteredClothes(
        token: String,
        request: RequestRegisteredClothes
    ): Call<List<ResponseRegisteredClothes>> {
        return closetDataSource.getRegisteredClothes(token, request)
    }

    override suspend fun deleteClothItem(token: String, clothId: Int): Call<Void> {
        return closetDataSource.deleteClothItem(token, clothId)
    }

    override suspend fun getRegisteredClothInfo(
        token: String,
        clothId: Int
    ): Call<ResponseRegisteredClothInfo> {
        return closetDataSource.getRegisteredClothInfo(token, clothId)
    }
}