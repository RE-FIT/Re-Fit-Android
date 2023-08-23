package com.example.refit.presentation.community

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.appcompat.widget.ListPopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.refit.R
import com.example.refit.databinding.FragmentCommunityBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DropdownMenuManager
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.community.adapter.CommunityListAdapter
import com.example.refit.presentation.community.viewmodel.CommunityInfoViewModel
import com.example.refit.presentation.community.viewmodel.CommunitySearchViewModel
import com.example.refit.presentation.community.viewmodel.CommunityViewModel
import com.example.refit.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class CommunityFragment : BaseFragment<FragmentCommunityBinding>(R.layout.fragment_community) {

    private val vm: CommunityViewModel by sharedViewModel()
    private val vmInfo: CommunityInfoViewModel by sharedViewModel()
    private val vmSearch: CommunitySearchViewModel by sharedViewModel()
    private var recyclerViewState: Parcelable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("[MAIN] onViewCreated")
        binding.vm = vm

        if (vm.scrollStatus.value != true) {
            vm.initStatus()
            vm.initCommunityList()
        }

        initCommunityList()
        initCommunityOptionDropdown()
        setClickedButton()
        observeStatus()
        vmSearch.setFirstOrNot(false)
    }

    override fun onResume() {
        super.onResume()
        Timber.d("[MAIN] onResume")
        if (recyclerViewState != null && vm.scrollStatus.value == true) {
            setSavedRecyclerViewState()
            vm.setScrollStatus(false)
        }
    }

    private fun initCommunityOptionDropdown() {
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

        binding.cvCommunityOptionCategory.setOnClickListener {
            val listPopupWindow = getPopupMenu(it, R.array.community_item_search_option_category)
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
                    vm.loadCommunityList()
                }

                binding.cvCommunityOptionGender.id -> {
                    binding.tvCommunityOptionGender.text = itemDescription
                    vm.setDropDownController(1, itemDescription)
                    vm.loadCommunityList()
                }

                binding.cvCommunityOptionCategory.id -> {
                    binding.tvCommunityOptionCategory.text = itemDescription
                    vm.setDropDownController(2, itemDescription)
                    vm.loadCommunityList()
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
            anchorView,
            R.style.ListPopupMenuWindow_CommunityOption,
            R.layout.list_popup_window_item_white,
            items
        )
    }

    private fun initCommunityList() {
        binding.rvCommunityList.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvCommunityList.adapter = CommunityListAdapter(vm).apply {
            vm.communityList.observe(viewLifecycleOwner) { list ->
                submitList(list)
            }
        }
    }

    private fun setClickedButton() {
        binding.ibCommunityMail.setOnClickListener {
            navigate(R.id.action_nav_community_to_chatRoomFragment)
        }

        binding.ibCommunitySearch.setOnClickListener {
            navigate(R.id.action_nav_community_to_communitySearchFragment)
        }

        binding.fabCommunityAdd.setOnClickListener {
            navigate(R.id.action_nav_community_to_communityAddPostFragment)
        }

    }

    private fun saveRecyclerViewState() {
        Timber.d("[MAIN] Saved 저장")
        recyclerViewState = binding.rvCommunityList.layoutManager!!.onSaveInstanceState()
    }

    private fun setSavedRecyclerViewState() {
        Timber.d("[MAIN] setSaved 실행")
        binding.rvCommunityList.layoutManager!!.onRestoreInstanceState(recyclerViewState)
    }

    private fun observeStatus() {
        vm.communityList.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                initCommunityList()
            }
        }

        vm.selectedPostItem.observe(viewLifecycleOwner, EventObserver { postId ->
            vmInfo.clickedGetPost(postId)
            saveRecyclerViewState()
            navigate(R.id.action_nav_community_to_communityInfoFragment)
        })
    }
}