package com.example.refit.presentation.community.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.refit.data.model.community.CommunityListItemResponse
import com.example.refit.databinding.ItemCommunityBinding

class CommunityListViewHolder(
    private val binding: ItemCommunityBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(communityListItemResponse: CommunityListItemResponse) {
            val deliveryType = communityListItemResponse.deliveryType
            val title = communityListItemResponse.title
            val gender = communityListItemResponse.gender
            val imgUrl = communityListItemResponse.imgUrl
            val postId = communityListItemResponse.postId
            val price = communityListItemResponse.price
            val region = communityListItemResponse.region
        }
}