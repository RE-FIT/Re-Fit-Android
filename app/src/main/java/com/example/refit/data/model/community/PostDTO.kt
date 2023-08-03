package com.example.refit.data.model.community

import com.google.gson.annotations.SerializedName

data class PostDTODt (
    @SerializedName("title")
    val title: String,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("postType")
    val postType: Int,
    @SerializedName("price")
    val price: Int,
    @SerializedName("category")
    val category: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("deliveryType")
    val deliveryType: Int,
    @SerializedName("deliveryFee")
    val deliveryFee: Int,
    @SerializedName("detail")
    val detail: String,
    @SerializedName("sido")
    val sido: String,
    @SerializedName("sigungu")
    val sigungu: String,
    @SerializedName("bname")
    val bname: String,
    @SerializedName("bname2")
    val bname2: String,
)

data class PostDTODelivery (
    @SerializedName("title")
    val title: String,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("postType")
    val postType: Int,
    @SerializedName("price")
    val price: Int,
    @SerializedName("category")
    val category: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("deliveryType")
    val deliveryType: Int,
    @SerializedName("deliveryFee")
    val deliveryFee: Int,
    @SerializedName("detail")
    val detail: String,
)