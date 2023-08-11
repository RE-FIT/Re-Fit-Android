package com.example.refit.presentation.community.adapter

import android.R
import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.refit.databinding.ItemSliderBinding
import com.example.refit.presentation.community.viewmodel.CommunityInfoViewModel


class InfoImageAdapter (private val vm: CommunityInfoViewModel) :
    RecyclerView.Adapter<InfoImageAdapter.MatchingListViewHolder>() {

    var sliderImageUrls: List<String> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchingListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSliderBinding.inflate(layoutInflater, parent, false)

        return MatchingListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MatchingListViewHolder,
        position: Int
    ) {
        holder.bindImage(sliderImageUrls[position])
    }

    override fun getItemCount(): Int {
        return sliderImageUrls.size
    }

    inner class MatchingListViewHolder(private val binding: ItemSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindImage(imageUrl: String) {
            Glide.with(binding.root)
                .load(imageUrl)
                .into(binding.communityInfoImageSlider)
        }
    }
}