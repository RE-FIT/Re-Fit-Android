package com.example.refit.presentation.closet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.refit.data.model.closet.RegisteredClothInfoResponse
import com.example.refit.databinding.ItemClothBinding
import com.example.refit.presentation.closet.viewmodel.ClosetViewModel

class UserRegisteredClothesAdapter(private val viewModel: ClosetViewModel) :
    ListAdapter<RegisteredClothInfoResponse, UserRegisteredClothesAdapter.MatchingListViewHolder>(
        RegisteredClothDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchingListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemClothBinding.inflate(layoutInflater, parent, false)
        return MatchingListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MatchingListViewHolder, position: Int) {
        holder.bindItems(getItem(position))
    }

    inner class MatchingListViewHolder(private val binding: ItemClothBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(clothInfo: RegisteredClothInfoResponse) {
            binding.clothInfo = clothInfo
            binding.vm = viewModel
            binding.executePendingBindings()
        }
    }

    class RegisteredClothDiffCallback() : DiffUtil.ItemCallback<RegisteredClothInfoResponse>() {
        override fun areItemsTheSame(
            oldItem: RegisteredClothInfoResponse,
            newItem: RegisteredClothInfoResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RegisteredClothInfoResponse,
            newItem: RegisteredClothInfoResponse
        ): Boolean {
            return oldItem == newItem
        }
    }

}