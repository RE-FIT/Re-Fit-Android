package com.example.refit.data.model.closet
import com.google.gson.annotations.SerializedName


data class ResponseQuizInfo(
    @SerializedName("answer")
    val answer: Boolean,
    @SerializedName("category")
    val category: Int,
    @SerializedName("explanation")
    val explanation: String,
    @SerializedName("question")
    val question: String
)