package com.example.refit.data.model.closet
import com.google.gson.annotations.SerializedName


data class RequestRegisteredClothes(
    @SerializedName("category")
    val category: Int,
    @SerializedName("season")
    val season: Int,
    @SerializedName("sort")
    val sort: String,
    @SerializedName("page")
    val page: Int,
    @SerializedName("size")
    val size: Int
)