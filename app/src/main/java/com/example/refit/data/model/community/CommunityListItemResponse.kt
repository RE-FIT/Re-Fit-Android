package com.example.refit.data.model.community

import com.google.gson.annotations.SerializedName

data class CommunityListItemResponse(
    @SerializedName("deliveryType")
    val deliveryType: Int,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("imgUrl")
    val imgUrl: String,
    @SerializedName("postId")
    val postId: Int,
    @SerializedName("price")
    val price: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("size")
    val size: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("scrapFlag")
    val scrapFlag: Boolean
)