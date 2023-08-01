package com.example.refit.data.repository.community.datasource

import com.example.refit.data.model.community.PostResponse
import com.example.refit.data.network.api.CommunityApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.File

class CommunityDataSourceImpl(private val communityApi: CommunityApi) : CommunityDataSource {

    override suspend fun loadCommunityList(accessToken: String): Call<ResponseBody> {
        return communityApi.loadCommunityList(accessToken)
    }

    override suspend fun loadCommunityListSort(
        accessToken: String,
        postType: Int,
        gender: Int,
        category: Int
    ): Call<ResponseBody> {
        return communityApi.loadCommunityList(accessToken, postType, gender, category)
    }

    override suspend fun createPostShareNDt(
        accessToken: String,
        title: String,
        gender: Int,
        postType: Int,
        category: Int,
        size: Int,
        deliveryType: Int,
        detail: String,
        sido: String,
        sigungu: String,
        bname: String,
        bname2: String,
        images: List<File>
    ): Call<ResponseBody> {
        val titleBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val genderBody = gender.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val postTypeBody = postType.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val categoryBody = category.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val sizeBody = size.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val deliveryTypeBody =
            deliveryType.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val detailBody = detail.toRequestBody("text/plain".toMediaTypeOrNull())
        val sidoBody = sido.toRequestBody("text/plain".toMediaTypeOrNull())
        val sigunguBody = sigungu.toRequestBody("text/plain".toMediaTypeOrNull())
        val bnameBody = bname.toRequestBody("text/plain".toMediaTypeOrNull())
        val bname2Body = bname2.toRequestBody("text/plain".toMediaTypeOrNull())

        val imageParts: List<MultipartBody.Part> = images.map { file ->
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("images", file.name, requestFile)
        }
        return communityApi.createPostShareNDt(
            accessToken,
            titleBody,
            genderBody,
            postTypeBody,
            categoryBody,
            sizeBody,
            deliveryTypeBody,
            detailBody,
            sidoBody,
            sigunguBody,
            bnameBody,
            bname2Body,
            imageParts
        )
    }

    override suspend fun createPostShareNDelivery(
        accessToken: String,
        title: String,
        gender: Int,
        postType: Int,
        category: Int,
        size: Int,
        deliveryType: Int,
        deliveryFee: Int,
        detail: String,
        images: List<File>
    ): Call<ResponseBody> {
        val titleBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val genderBody = gender.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val postTypeBody = postType.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val categoryBody = category.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val sizeBody = size.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val deliveryTypeBody =
            deliveryType.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val deliveryFeeBody = deliveryFee.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val detailBody = detail.toRequestBody("text/plain".toMediaTypeOrNull())


        val imageParts: List<MultipartBody.Part> = images.map { file ->
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("images", file.name, requestFile)
        }
        return communityApi.createPostShareNDelivery(
            accessToken, titleBody, genderBody, postTypeBody, categoryBody,
            sizeBody, deliveryTypeBody, deliveryFeeBody, detailBody, imageParts
        )
    }

    override suspend fun createPostSaleNDt(
        accessToken: String,
        title: String,
        gender: Int,
        postType: Int,
        price: Int,
        category: Int,
        size: Int,
        deliveryType: Int,
        detail: String,
        sido: String,
        sigungu: String,
        bname: String,
        bname2: String,
        images: List<File>
    ): Call<ResponseBody> {
        val titleBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val genderBody = gender.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val postTypeBody = postType.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val priceBody = price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val categoryBody = category.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val sizeBody = size.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val deliveryTypeBody =
            deliveryType.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val detailBody = detail.toRequestBody("text/plain".toMediaTypeOrNull())
        val sidoBody = sido.toRequestBody("text/plain".toMediaTypeOrNull())
        val sigunguBody = sigungu.toRequestBody("text/plain".toMediaTypeOrNull())
        val bnameBody = bname.toRequestBody("text/plain".toMediaTypeOrNull())
        val bname2Body = bname2.toRequestBody("text/plain".toMediaTypeOrNull())

        val imageParts: List<MultipartBody.Part> = images.map { file ->
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("images", file.name, requestFile)
        }
        return communityApi.createPostSaleNDt(
            accessToken,
            titleBody,
            genderBody,
            postTypeBody,
            priceBody,
            categoryBody,
            sizeBody,
            deliveryTypeBody,
            detailBody,
            sidoBody,
            sigunguBody,
            bnameBody,
            bname2Body,
            imageParts
        )
    }


    override suspend fun createPostSaleNDelivery(
        accessToken: String,
        title: String,
        gender: Int,
        postType: Int,
        price: Int,
        category: Int,
        size: Int,
        deliveryType: Int,
        deliveryFee: Int,
        detail: String,
        images: List<File>
    ): Call<ResponseBody> {
        val titleBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val genderBody = gender.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val postTypeBody = postType.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val priceBody = price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val categoryBody = category.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val sizeBody = size.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val deliveryTypeBody =
            deliveryType.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val deliveryFeeBody = deliveryFee.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val detailBody = detail.toRequestBody("text/plain".toMediaTypeOrNull())


        val imageParts: List<MultipartBody.Part> = images.map { file ->
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("images", file.name, requestFile)
        }
        return communityApi.createPostSaleNDelivery(
            accessToken, titleBody, genderBody, postTypeBody, priceBody, categoryBody,
            sizeBody, deliveryTypeBody, deliveryFeeBody, detailBody, imageParts
        )
    }


    override suspend fun getPost(accessToken: String, postId: Int): Call<PostResponse> {
        return communityApi.getPost(accessToken, postId)
    }

    override suspend fun loadSearchResult(
        accessToken: String,
        keyword: String
    ): Call<ResponseBody> {
        return communityApi.loadSearchResult(accessToken, keyword)
    }

}