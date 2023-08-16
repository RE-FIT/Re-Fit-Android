package com.example.refit.data.model.community

import com.google.gson.annotations.SerializedName

data class PostResponse(
    @SerializedName("postId") val postId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("author") val author: String,
    @SerializedName("imgUrls") val imgUrls: List<String>,
    @SerializedName("gender") val gender: Int,
    @SerializedName("size") val size: Int,
    @SerializedName("category") val category: Int,
    @SerializedName("deliveryType") val deliveryType: Int,
    @SerializedName("deliveryFee") val deliveryFee: Int,
    @SerializedName("address") val address: String?,
    @SerializedName("price") val price: Int,
    @SerializedName("detail") val detail: String,
    @SerializedName("postType") val postType: Int,
    @SerializedName("postState") val postState: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("clickedMember") val clickedMember: String,
    @SerializedName("scrapFlag") val scrapFlag: Boolean,
    @SerializedName("profileUrl") val profileUrl: String? = ""
)
