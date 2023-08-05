package com.example.refit.data.repository.colset.datasource

import com.example.refit.data.model.closet.RequestRegisteredClothes
import com.example.refit.data.model.closet.ResponseAddNewCloth
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

}