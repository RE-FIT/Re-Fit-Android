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
    @SerializedName("region")
    val region: String,
    @SerializedName("title")
    val title: String
)