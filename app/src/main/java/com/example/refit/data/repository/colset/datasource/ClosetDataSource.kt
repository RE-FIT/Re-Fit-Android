package com.example.refit.data.repository.colset.datasource

import com.example.refit.data.model.closet.RequestAddNewCloth
import com.example.refit.data.model.closet.RequestRegisteredClothes
import com.example.refit.data.model.closet.RequestResetCompletedCloth
import com.example.refit.data.model.closet.ResponseAddNewCloth
import com.example.refit.data.model.closet.ResponseRegisteredClothInfo
import com.example.refit.data.model.closet.ResponseRegisteredClothes
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call

interface ClosetDataSource {

    suspend fun addNewCloth(
        token: String,
        image: MultipartBody.Part,
        request: RequestBody
    ): Call<Long>

    suspend fun getRegisteredClothes(
        token: String,
        request: RequestRegisteredClothes
    ): Call<List<ResponseRegisteredClothes>>

    suspend fun deleteClothItem(
        token: String,
        clothId: Int
    ): Call<Void>

    suspend fun getRegisteredClothInfo(
        token: String,
        clothId: Int
    ): Call<ResponseRegisteredClothInfo>

    suspend fun fixClothItem(
        token: String,
        request: RequestAddNewCloth,
        clothId: Int
    ): Call<Void>

    suspend fun resetCompletedCloth(
        token: String,
        request: RequestResetCompletedCloth,
        clothId: Int
    ): Call<Void>

    suspend fun wearClothes(
        token: String,
        id: Int
    ): Call<Void>

}