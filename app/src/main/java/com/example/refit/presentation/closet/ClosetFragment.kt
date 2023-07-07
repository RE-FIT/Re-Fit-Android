package com.example.refit.presentation.closet

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.appcompat.widget.ListPopupWindow
import androidx.recyclerview.widget.GridLayoutManager
import com.example.refit.R
import com.example.refit.databinding.FragmentClosetBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DropdownMenuManager
import com.google.android.material.card.MaterialCardView
import timber.log.Timber

class ClosetFragment : BaseFragment<FragmentClosetBinding>(R.layout.fragment_closet) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDefaultClothCategory(binding.cvClosetCategoryContainerTop)
        initClosetOptionPopupMenu()
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

    private fun initClosetOptionPopupMenu() {
        binding.cvClosetOptionSeason.setOnClickListener {
            val listPopupWindow = getPopupMenu(it, R.array.closet_item_search_option_season)
            setPopupItemClickListener(it.id, listPopupWindow)
            listPopupWindow.show()
        }

        binding.cvClosetOptionSort.setOnClickListener {
            val listPopupWindow = getPopupMenu(it, R.array.closet_item_search_option_sort)
            setPopupItemClickListener(it.id, listPopupWindow)
            listPopupWindow.show()
        }
    }

    private fun setPopupItemClickListener(viewId: Int, popupMenu: ListPopupWindow) {
        popupMenu.setOnItemClickListener { _, view, _, _ ->
            val itemDescription = (view as TextView).text.toString()
            when (viewId) {
                // TODO(기능 구현 때 뷰모델과 연동)
                binding.cvClosetOptionSeason.id -> {
                    binding.tvClosetOptionSeason.text = itemDescription
                    Timber.d(itemDescription)
                }

                binding.cvClosetOptionSort.id -> {
                    binding.tvClosetOptionSort.text = itemDescription
                    Timber.d(itemDescription)
                }
            }
            popupMenu.dismiss()
        }
    }

    private fun getPopupMenu(
        anchorView: View,
        @ArrayRes items: Int,
    ): ListPopupWindow {
        return DropdownMenuManager.createPopupMenu(
            requireActivity(),
            anchorView,
            R.style.ListPopupMenuWindow_ClosetOption,
            R.layout.list_popup_window_item_white,
            items
        )
    }
}