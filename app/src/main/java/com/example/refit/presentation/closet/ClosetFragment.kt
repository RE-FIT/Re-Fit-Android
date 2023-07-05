package com.example.refit.presentation.closet

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.GridLayoutManager
import com.example.refit.R
import com.example.refit.databinding.FragmentClosetBinding
import com.example.refit.presentation.common.BaseFragment
import com.google.android.material.card.MaterialCardView

class ClosetFragment : BaseFragment<FragmentClosetBinding>(R.layout.fragment_closet) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDefaultClothCategory(binding.cvClosetCategoryContainerTop)
        handleClothesCategory()

        binding.rvClosetList.layoutManager = GridLayoutManager(requireActivity(), 2)
    }

    private fun initDefaultClothCategory(cardView: MaterialCardView) {
        binding.selectedCategoryId = cardView.getChildAt(0).id
    }

    private fun handleClothesCategory() {
        binding.onClickCardViewListener = OnClickListener { view ->
            val selectedView = view as MaterialCardView
            binding.selectedCategoryId = selectedView.getChildAt(0).id
        }
    }
}