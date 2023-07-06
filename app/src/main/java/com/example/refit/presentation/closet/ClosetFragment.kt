package com.example.refit.presentation.closet

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import androidx.annotation.MenuRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.GridLayoutManager
import com.example.refit.R
import com.example.refit.databinding.FragmentClosetBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.CustomSnackBar
import com.google.android.material.card.MaterialCardView
import timber.log.Timber

class ClosetFragment : BaseFragment<FragmentClosetBinding>(R.layout.fragment_closet) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDefaultClothCategory(binding.cvClosetCategoryContainerTop)
        initClosetOptionPopupMenu()
        handleClothesCategory()
        binding.rvClosetList.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.fabClosetAdd.setOnClickListener {
            CustomSnackBar.make(binding.root, "안녕하세요", R.anim.anim_show_snack_bar_from_top).show()
        }
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

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val wrapper = ContextThemeWrapper(requireActivity(), R.style.PopupMenu_ClosetOption)
        val popup = PopupMenu(wrapper, v)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            Timber.d(" 옵션 ${menuItem.title}")
            when (menuItem.itemId) {
                R.id.option_closet_season_first,
                R.id.option_closet_season_second,
                R.id.option_closet_season_third -> Timber.d("${menuItem.title}")

                R.id.option_closet_sort_first,
                R.id.option_closet_sort_second,
                R.id.option_closet_sort_third -> Timber.d("${menuItem.title}")
            }
            true
        }
        popup.setOnDismissListener {
        }
        popup.show()
    }

    private fun initClosetOptionPopupMenu() {
        binding.cvClosetOptionSeason.setOnClickListener {
            showMenu(it, R.menu.menu_closet_search_option_season)
        }
        binding.cvClosetOptionSort.setOnClickListener {
            showMenu(it, R.menu.menu_closet_search_option_sort)
        }
    }
}