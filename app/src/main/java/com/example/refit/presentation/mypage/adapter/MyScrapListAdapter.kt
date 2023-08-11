package com.example.refit.presentation.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.refit.data.model.community.CommunityListItemResponse
import com.example.refit.data.model.mypage.MyScrapSellListItemResponse
import com.example.refit.databinding.ItemScrapBinding
import com.example.refit.presentation.mypage.viewmodel.MyScrapViewModel

class MyScrapListAdapter(private val viewModel: MyScrapViewModel) :
    ListAdapter<MyScrapSellListItemResponse, MyScrapListAdapter.MatchingListViewHolder>(
        MyScrapItemListDiffCallback()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyScrapListAdapter.MatchingListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemScrapBinding.inflate(layoutInflater, parent, false)
        return MatchingListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MyScrapListAdapter.MatchingListViewHolder,
        position: Int
    ) {
        holder.bindItems(getItem(position))
    }

    inner class MatchingListViewHolder(private val binding: ItemScrapBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(scrapList: MyScrapSellListItemResponse) {
            binding.scrapList = scrapList
            binding.vm = viewModel
            binding.executePendingBindings()
        }
    }

    class MyScrapItemListDiffCallback() : DiffUtil.ItemCallback<MyScrapSellListItemResponse>() {
        override fun areItemsTheSame(
            oldItem: MyScrapSellListItemResponse,
            newItem: MyScrapSellListItemResponse
        ): Boolean {
            return oldItem.postId == newItem.postId
        }

        override fun areContentsTheSame(
            oldItem: MyScrapSellListItemResponse,
            newItem: MyScrapSellListItemResponse
        ): Boolean {
            return oldItem == newItem
        }
    }
}