package com.example.refit.presentation.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.refit.data.model.mypage.MyFeedGiveListItemResponse
import com.example.refit.data.model.mypage.MyFeedSellListItemResponse
import com.example.refit.databinding.ItemFeedBinding
import com.example.refit.databinding.ItemFeedSellBinding
import com.example.refit.presentation.mypage.viewmodel.MyFeedViewModel

class MyFeedSellListAdapter(private val viewModel: MyFeedViewModel) :
    ListAdapter<MyFeedSellListItemResponse, MyFeedSellListAdapter.MatchingListViewHolder>(
        MyFeedItemListDiffCallback()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyFeedSellListAdapter.MatchingListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFeedSellBinding.inflate(layoutInflater, parent, false)
        return MatchingListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MyFeedSellListAdapter.MatchingListViewHolder,
        position: Int
    ) {
        holder.bindItems(getItem(position))
    }

    inner class MatchingListViewHolder(private val binding: ItemFeedSellBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(feedList: MyFeedSellListItemResponse) {
            binding.feedList = feedList
            binding.vm = viewModel
            binding.executePendingBindings()

            binding.mcvCommunityItemHolder.setOnClickListener {
                viewModel.handleClickItem(feedList.postId)
            }
        }
    }

    class MyFeedItemListDiffCallback() : DiffUtil.ItemCallback<MyFeedSellListItemResponse>() {
        override fun areItemsTheSame(
            oldItem: MyFeedSellListItemResponse,
            newItem: MyFeedSellListItemResponse
        ): Boolean {
            return oldItem.postId == newItem.postId
        }

        override fun areContentsTheSame(
            oldItem: MyFeedSellListItemResponse,
            newItem: MyFeedSellListItemResponse
        ): Boolean {
            return oldItem == newItem
        }
    }
}