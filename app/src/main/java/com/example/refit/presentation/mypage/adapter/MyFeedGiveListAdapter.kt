package com.example.refit.presentation.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.refit.data.model.mypage.MyFeedGiveListItemResponse
import com.example.refit.databinding.ItemFeedBinding
import com.example.refit.presentation.mypage.viewmodel.MyFeedViewModel

class MyFeedGiveListAdapter(private val viewModel: MyFeedViewModel) :
    ListAdapter<MyFeedGiveListItemResponse, MyFeedGiveListAdapter.MatchingListViewHolder>(
        MyFeedItemListDiffCallback()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyFeedGiveListAdapter.MatchingListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFeedBinding.inflate(layoutInflater, parent, false)
        return MatchingListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MyFeedGiveListAdapter.MatchingListViewHolder,
        position: Int
    ) {
        holder.bindItems(getItem(position))
    }

    inner class MatchingListViewHolder(private val binding: ItemFeedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(feedList: MyFeedGiveListItemResponse) {
            binding.feedList = feedList
            binding.vm = viewModel
            binding.executePendingBindings()

            binding.mcvCommunityItemHolder.setOnClickListener {
                viewModel.handleClickItem(feedList.postId)
            }
        }
    }

    class MyFeedItemListDiffCallback() : DiffUtil.ItemCallback<MyFeedGiveListItemResponse>() {
        override fun areItemsTheSame(
            oldItem: MyFeedGiveListItemResponse,
            newItem: MyFeedGiveListItemResponse
        ): Boolean {
            return oldItem.postId == newItem.postId
        }

        override fun areContentsTheSame(
            oldItem: MyFeedGiveListItemResponse,
            newItem: MyFeedGiveListItemResponse
        ): Boolean {
            return oldItem == newItem
        }
    }
}