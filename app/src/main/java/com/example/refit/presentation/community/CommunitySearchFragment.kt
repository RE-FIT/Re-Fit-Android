package com.example.refit.presentation.community

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.appcompat.widget.ListPopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.refit.R
import com.example.refit.databinding.FragmentCommunitySearchBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DropdownMenuManager
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.common.NavigationUtil.navigateUp
import com.example.refit.presentation.community.adapter.SearchListAdapter
import com.example.refit.presentation.community.viewmodel.CommunityInfoViewModel
import com.example.refit.presentation.community.viewmodel.CommunitySearchViewModel
import com.example.refit.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CommunitySearchFragment : BaseFragment<FragmentCommunitySearchBinding>(R.layout.fragment_community_search) {

    private val vm: CommunitySearchViewModel by sharedViewModel()
    private val vminfo: CommunityInfoViewModel by sharedViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = vm

        vm.initStatus()
        initSearchList()
        setOnClickedButton()
        initSearchOptionDropdown()
        observeEditTextChanges()
        observeStatus()
    }


    private fun setOnClickedButton() {
        binding.ibCommunitySearchBack.setOnClickListener {
            navigateUp()
        }

        binding.ibCommunitySearchButton.setOnClickListener {
            vm.setSearchingState(true)
            val keyword = binding.etCommunitySearchKeyword.text.toString()
            vm.setKeyword(keyword)
            vm.loadSearchResult()
        }

        binding.ibCommunitySearchClose.setOnClickListener {
            binding.etCommunitySearchKeyword.setText("")
            vm.setSearchingState(false)
            vm.setExistResult(false)
            vm.setExistResult(false)
        }

        binding.ibGoToCommunity.setOnClickListener {
            navigateUp()
        }
    }

    private fun initSearchOptionDropdown() {
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
        popupMenu.setOnItemClickListener { _, view, _, _ ->
            val itemDescription = (view as TextView).text.toString()
            when (viewId) {
                binding.cvCommunityOptionType.id -> {
                    binding.tvCommunityOptionType.text = itemDescription
                    vm.setDropDownController(0, itemDescription)
                    vm.loadSearchResult()
                }

                binding.cvCommunityOptionGender.id -> {
                    binding.tvCommunityOptionGender.text = itemDescription
                    vm.setDropDownController(1, itemDescription)
                    vm.loadSearchResult()
                }

                binding.cvCommunityOptionCategory.id -> {
                    binding.tvCommunityOptionCategory.text = itemDescription
                    vm.setDropDownController(2, itemDescription)
                    vm.loadSearchResult()
                }
            }
            vm.loadSearchResult()
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

    private fun initSearchList() {
        binding.rvSearchList.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvSearchList.adapter = SearchListAdapter(vm).apply {
            vm.searchList.observe(viewLifecycleOwner) { list ->
                submitList(list)
            }
        }
    }

    private fun observeStatus() {
        vm.selectedPostItem.observe(viewLifecycleOwner, EventObserver { postId ->
            vminfo.clickedGetPost(postId)
            navigate(R.id.action_communitySearchFragment_to_communityInfoFragment)
        })
    }

}

