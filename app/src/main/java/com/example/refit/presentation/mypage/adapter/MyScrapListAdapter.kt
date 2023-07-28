package com.example.refit.presentation.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.refit.data.model.community.CommunityListItemResponse
import com.example.refit.databinding.ItemCommunityBinding
import com.example.refit.presentation.community.viewmodel.CommunityViewModel

class MyScrapListAdapter(private val viewModel: CommunityViewModel) :
    ListAdapter<CommunityListItemResponse, MyScrapListAdapter.MatchingListViewHolder>(
        CommunityItemListDiffCallback()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyScrapListAdapter.MatchingListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCommunityBinding.inflate(layoutInflater, parent, false)
        return MatchingListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MyScrapListAdapter.MatchingListViewHolder,
        position: Int
    ) {
        holder.bindItems(getItem(position))
    }

    inner class MatchingListViewHolder(private val binding: ItemCommunityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(communityList: CommunityListItemResponse) {
            binding.communityList = communityList
            binding.vm = viewModel
            binding.executePendingBindings()
        }
    }

    class CommunityItemListDiffCallback() : DiffUtil.ItemCallback<CommunityListItemResponse>() {
        override fun areItemsTheSame(
            oldItem: CommunityListItemResponse,
            newItem: CommunityListItemResponse
        ): Boolean {
            return oldItem.postId == newItem.postId
        }

        override fun areContentsTheSame(
            oldItem: CommunityListItemResponse,
            newItem: CommunityListItemResponse
        ): Boolean {
            return oldItem == newItem
        }
    }
}