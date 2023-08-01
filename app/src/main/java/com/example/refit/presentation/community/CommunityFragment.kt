package com.example.refit.presentation.community

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.appcompat.widget.ListPopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.refit.R
import com.example.refit.databinding.FragmentCommunityBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DropdownMenuManager
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.common.WindowUtil.setStatusBarColor
import com.example.refit.presentation.community.adapter.CommunityListAdapter
import com.example.refit.presentation.community.viewmodel.CommunityAddPostViewModel
import com.example.refit.presentation.community.viewmodel.CommunityViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class CommunityFragment : BaseFragment<FragmentCommunityBinding>(R.layout.fragment_community) {

    private val communityViewModel: CommunityViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = communityViewModel

        communityViewModel.getCommunityList()
        initCommunityList()
        initCommunityOptionDropdown()
        setClickedButton()
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

    private fun initCommunityList() {
        binding.rvCommunityList.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvCommunityList.adapter = CommunityListAdapter(communityViewModel).apply {
            communityViewModel.communityList.observe(viewLifecycleOwner) { list ->
                submitList(list)
            }
        }
    }

    private fun setClickedButton() {
        binding.ibCommunityMail.setOnClickListener {
            // TODO (새로운 채팅이 있으면 N)
        }

        // 페이지 이동 임시 코드
        binding.ibCommunitySearch.setOnClickListener {
            navigate(R.id.action_nav_community_to_communityInfoFragment)
        }

        binding.fabCommunityAdd.setOnClickListener {
            navigate(R.id.action_nav_community_to_communityAddPostFragment)
        }

    }
}