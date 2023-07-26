package com.example.refit.data.model.community

data class CommunityListItemResponse(
    val postId: Int,
    val title: String,
    // val imgUrl: Uri,
    val gender: Int,
    val deliveryType: Int,
    val region: String,
    val price: Int
    )