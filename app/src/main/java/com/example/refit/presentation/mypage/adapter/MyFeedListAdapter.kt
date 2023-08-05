package com.example.refit.presentation.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.refit.data.model.community.CommunityListItemResponse
import com.example.refit.databinding.ItemFeedBinding
import com.example.refit.presentation.mypage.viewmodel.MyFeedViewModel

class MyFeedListAdapter(private val viewModel: MyFeedViewModel) :
    ListAdapter<CommunityListItemResponse, MyFeedListAdapter.MatchingListViewHolder>(
        MyFeedItemListDiffCallback()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyFeedListAdapter.MatchingListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFeedBinding.inflate(layoutInflater, parent, false)
        return MatchingListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MyFeedListAdapter.MatchingListViewHolder,
        position: Int
    ) {
        holder.bindItems(getItem(position))
    }

    inner class MatchingListViewHolder(private val binding: ItemFeedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(feedList: CommunityListItemResponse) {
            binding.communityList = feedList
            binding.vm = viewModel
            binding.executePendingBindings()
        }
    }

    class MyFeedItemListDiffCallback() : DiffUtil.ItemCallback<CommunityListItemResponse>() {
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