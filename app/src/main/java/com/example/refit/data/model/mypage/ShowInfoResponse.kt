package com.example.refit.data.model.mypage

data class ShowInfoResponse (
    private val imageUrl: String,
    private val email: String,
    private val loginId: String,
    private val name: String,
    private val birth: String,
    private val gender: Int
)