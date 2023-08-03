package com.example.refit.presentation.community.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.refit.data.model.community.CommunityListItemResponse
import com.example.refit.databinding.ItemCommunityBinding
import com.example.refit.presentation.community.viewmodel.CommunitySearchViewModel
import com.example.refit.presentation.community.viewmodel.CommunityViewModel
import com.example.refit.presentation.mypage.viewmodel.MyScrapViewModel

class SearchListAdapter(private val viewModel: CommunitySearchViewModel) :
    ListAdapter<CommunityListItemResponse, SearchListAdapter.MatchingListViewHolder>(
        SearchItemListDiffCallback()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchListAdapter.MatchingListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCommunityBinding.inflate(layoutInflater, parent, false)
        return MatchingListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SearchListAdapter.MatchingListViewHolder,
        position: Int
    ) {
        holder.bindItems(getItem(position))
    }

    inner class MatchingListViewHolder(private val binding: ItemCommunityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(searchList: CommunityListItemResponse) {
            binding.communityList = searchList
            binding.vmSearch = viewModel
            binding.executePendingBindings()

            binding.mcvCommunityItemHolder.setOnClickListener {
                viewModel.handleClickItem(searchList.postId)
            }
        }
    }

    class SearchItemListDiffCallback() : DiffUtil.ItemCallback<CommunityListItemResponse>() {
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