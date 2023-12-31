package com.example.refit.presentation.closet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.refit.data.model.closet.ForestStamps
import com.example.refit.databinding.ItemForestStampBinding
import com.example.refit.presentation.closet.viewmodel.ForestViewModel

class ForestStampAdapter(private val viewModel: ForestViewModel) :
    ListAdapter<ForestStamps, ForestStampAdapter.ForestStampViewHolder>(
        ForestStampDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForestStampViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemForestStampBinding.inflate(layoutInflater, parent, false)
        return ForestStampViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForestStampViewHolder, position: Int) {
        holder.bindItems(getItem(position))
    }

    inner class ForestStampViewHolder(private val binding: ItemForestStampBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(stampInfo: ForestStamps) {
            binding.forestInfo = viewModel.forestInfo.value!!.content
            binding.stampInfo = stampInfo
            binding.vm = viewModel
            binding.executePendingBindings()
        }
    }

    class ForestStampDiffCallback() : DiffUtil.ItemCallback<ForestStamps>() {
        override fun areItemsTheSame(
            oldItem: ForestStamps,
            newItem: ForestStamps
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ForestStamps,
            newItem: ForestStamps
        ): Boolean {
            return oldItem == newItem
        }
    }
}