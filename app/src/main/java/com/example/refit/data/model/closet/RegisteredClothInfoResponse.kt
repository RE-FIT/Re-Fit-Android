package com.example.refit.data.model.closet

data class RegisteredClothInfoResponse(
    val id: Int,
    val dDay: Int,
    val fit: Int,
    val goal: Int,
    val wearingMonth: Int,
    val wearingWeek: Int,
    val progress: Int
)
