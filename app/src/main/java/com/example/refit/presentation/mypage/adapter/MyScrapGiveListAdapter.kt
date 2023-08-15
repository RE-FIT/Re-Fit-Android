package com.example.refit.presentation.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.refit.data.model.community.CommunityListItemResponse
import com.example.refit.data.model.mypage.MyScrapGiveListItemResponse
import com.example.refit.data.model.mypage.MyScrapSellListItemResponse
import com.example.refit.databinding.ItemScrapBinding
import com.example.refit.presentation.mypage.viewmodel.MyScrapViewModel

class MyScrapGiveListAdapter(private val viewModel: MyScrapViewModel) :
    ListAdapter<MyScrapGiveListItemResponse, MyScrapGiveListAdapter.MatchingListViewHolder>(
        MyScrapItemListDiffCallback()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyScrapGiveListAdapter.MatchingListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemScrapBinding.inflate(layoutInflater, parent, false)
        return MatchingListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MyScrapGiveListAdapter.MatchingListViewHolder,
        position: Int
    ) {
        holder.bindItems(getItem(position))
    }

    inner class MatchingListViewHolder(private val binding: ItemScrapBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(scrapList: MyScrapGiveListItemResponse) {
            binding.scrapList = scrapList
            binding.vm = viewModel
            binding.executePendingBindings()

            binding.mcvCommunityItemHolder.setOnClickListener {
                viewModel.handleClickItem(scrapList.postId)
            }
        }
    }

    class MyScrapItemListDiffCallback() : DiffUtil.ItemCallback<MyScrapGiveListItemResponse>() {
        override fun areItemsTheSame(
            oldItem: MyScrapGiveListItemResponse,
            newItem: MyScrapGiveListItemResponse
        ): Boolean {
            return oldItem.postId == newItem.postId
        }

        override fun areContentsTheSame(
            oldItem: MyScrapGiveListItemResponse,
            newItem: MyScrapGiveListItemResponse
        ): Boolean {
            return oldItem == newItem
        }
    }
}