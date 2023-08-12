package com.example.refit.presentation.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.refit.data.model.mypage.MyFeedBuyListItemResponse
import com.example.refit.data.model.mypage.MyFeedGiveListItemResponse
import com.example.refit.databinding.ItemFeedBinding
import com.example.refit.databinding.ItemFeedBuyBinding
import com.example.refit.presentation.mypage.viewmodel.MyFeedViewModel

class MyFeedBuyListAdapter(private val viewModel: MyFeedViewModel) :
    ListAdapter<MyFeedBuyListItemResponse, MyFeedBuyListAdapter.MatchingListViewHolder>(
        MyFeedItemListDiffCallback()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyFeedBuyListAdapter.MatchingListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFeedBuyBinding.inflate(layoutInflater, parent, false)
        return MatchingListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MyFeedBuyListAdapter.MatchingListViewHolder,
        position: Int
    ) {
        holder.bindItems(getItem(position))
    }

    inner class MatchingListViewHolder(private val binding: ItemFeedBuyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(feedList: MyFeedBuyListItemResponse) {
            binding.feedList = feedList
            binding.vm = viewModel
            binding.executePendingBindings()
        }
    }

    class MyFeedItemListDiffCallback() : DiffUtil.ItemCallback<MyFeedBuyListItemResponse>() {
        override fun areItemsTheSame(
            oldItem: MyFeedBuyListItemResponse,
            newItem: MyFeedBuyListItemResponse
        ): Boolean {
            return oldItem.postId == newItem.postId
        }

        override fun areContentsTheSame(
            oldItem: MyFeedBuyListItemResponse,
            newItem: MyFeedBuyListItemResponse
        ): Boolean {
            return oldItem == newItem
        }
    }
}