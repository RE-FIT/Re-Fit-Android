package com.example.refit.presentation.community

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.appcompat.widget.ListPopupWindow
import com.example.refit.R
import com.example.refit.databinding.FragmentCommunityBinding
import com.example.refit.databinding.FragmentCommunitySearchBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DropdownMenuManager
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.common.NavigationUtil.navigateUp
import com.example.refit.presentation.community.viewmodel.CommunityInfoViewModel
import com.example.refit.presentation.community.viewmodel.CommunitySearchViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class CommunitySearchFragment : BaseFragment<FragmentCommunitySearchBinding>(R.layout.fragment_community_search) {

    private val vm: CommunitySearchViewModel by sharedViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = vm

        vm.initStatus()
        setOnClickedButton()
        initCommunityOptionDropdown()
        observeEditTextChanges()
    }


    private fun setOnClickedButton() {
        binding.ibCommunitySearchBack.setOnClickListener {
            navigateUp()
        }

        binding.ibCommunitySearchButton.setOnClickListener {
            vm.setSearchingState(true)
            val keyword = binding.etCommunitySearchKeyword.text.toString()
            vm.loadSearchResult(keyword)
        }

        binding.ibGoToCommunity.setOnClickListener {
            navigateUp()
        }
    }

    private fun initCommunityOptionDropdown() {
        binding.cvCommunityOptionCategory.setOnClickListener {
            val listPopupWindow = getPopupMenu(it, R.array.community_item_search_option_category)
            setPopupItemClickListener(it.id, listPopupWindow)
            listPopupWindow.show()
        }

        binding.cvCommunityOptionType.setOnClickListener {
            val listPopupWindow = getPopupMenu(it, R.array.community_item_search_option_type)
            setPopupItemClickListener(it.id, listPopupWindow)
            listPopupWindow.show()
        }

        binding.cvCommunityOptionGender.setOnClickListener {
            val listPopupWindow = getPopupMenu(it, R.array.community_item_search_option_gender)
            setPopupItemClickListener(it.id, listPopupWindow)
            listPopupWindow.show()
        }
    }

    private fun setPopupItemClickListener(viewId: Int, popupMenu: ListPopupWindow) {
        val textView: TextView = when (viewId) {
            binding.cvCommunityOptionCategory.id -> binding.tvCommunityOptionCategory
            binding.cvCommunityOptionType.id -> binding.tvCommunityOptionType
            binding.cvCommunityOptionGender.id -> binding.tvCommunityOptionGender
            else -> return
        }

        popupMenu.setOnItemClickListener { _, view, _, _ ->
            val itemDescription = (view as TextView).text.toString()
            textView.text = itemDescription
            Timber.d(itemDescription)
            popupMenu.dismiss()
        }
    }

    private fun getPopupMenu(
        anchorView: View,
        @ArrayRes items: Int,
    ): ListPopupWindow {
        return DropdownMenuManager.createPopupMenu(
            anchorView,
            R.style.ListPopupMenuWindow_CommunityOption,
            R.layout.list_popup_window_item_white,
            items
        )
    }

    private fun observeEditTextChanges() {
        binding.etCommunitySearchKeyword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isFilled = s?.isNotEmpty() == true
                vm.setSearchTypingState(isFilled)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
}