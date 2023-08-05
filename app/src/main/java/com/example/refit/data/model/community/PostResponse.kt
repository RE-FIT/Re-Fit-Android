package com.example.refit.data.model.community

import com.google.gson.annotations.SerializedName

data class PostResponse(
    @SerializedName("postId") val postId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("author") val author: String,
    @SerializedName("imgUrls") val imgUrls: List<String>,
    @SerializedName("size") val size: Int,
    @SerializedName("deliveryType") val deliveryType: Int,
    @SerializedName("deliveryFee") val deliveryFee: Int,
    @SerializedName("sido") val sido: String?,
    @SerializedName("sigungu") val sigungu: String?,
    @SerializedName("bname") val bname: String?,
    @SerializedName("bname2") val bname2: String?,
    @SerializedName("price") val price: Int,
    @SerializedName("detail") val detail: String,
    @SerializedName("postType") val postType: Int,
    @SerializedName("postState") val postState: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("clickedMember") val clickedMember: String,
    @SerializedName("scrapFlag") val scrapFlag: Boolean
)
