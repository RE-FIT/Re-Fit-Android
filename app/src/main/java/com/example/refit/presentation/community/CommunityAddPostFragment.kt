package com.example.refit.presentation.community

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.appcompat.widget.ListPopupWindow
import com.example.refit.R
import com.example.refit.databinding.FragmentCommunityAddPostBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DropdownMenuManager
import com.example.refit.presentation.community.viewmodel.CommunityAddPostViewModel
import com.google.android.material.chip.Chip
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


class CommunityAddPostFragment :
    BaseFragment<FragmentCommunityAddPostBinding>(R.layout.fragment_community_add_post) {

    private val communityAddPostViewModel: CommunityAddPostViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cmviewmodel = communityAddPostViewModel
        CommunityAddContentsOptionDropdown()
        selectTransactionType()
    }


    // 추천 착용 성별, 카테고리, 사이즈, 배송 관련 드롭다운
    private fun CommunityAddContentsOptionDropdown() {
        binding.cvCommunityAddpostRecommendGender.setOnClickListener {
            communityAddPostViewModel.setClickedOptionRG(clicked = true)
            val listPopupWindow = getPopupMenu(it, R.array.community_item_search_option_gender)
            setPopupItemClickListener(it.id, listPopupWindow)
            listPopupWindow.show()
        }

        binding.cvCommunityAddpostClothesCategory.setOnClickListener {
            communityAddPostViewModel.setClickedOptionCategory(clicked = true)
            val listPopupWindow = getPopupMenu(it, R.array.community_item_search_option_category)
            setPopupItemClickListener(it.id, listPopupWindow)
            listPopupWindow.show()
        }

        binding.cvCommuntiyAddpostSize.setOnClickListener {
            communityAddPostViewModel.setClickedOptionSize(clicked = true)
            val listPopupWindow = getPopupMenu(it, R.array.community_item_search_option_size)
            setPopupItemClickListener(it.id, listPopupWindow)
            listPopupWindow.show()
        }

        binding.cvCommunityAddpostTransactionMethod.setOnClickListener {
            communityAddPostViewModel.setClickedOptionTM(clicked = true)
            val listPopupWindow =
                getPopupMenu(it, R.array.community_item_search_option_transaction_method)
            setPopupItemClickListener(it.id, listPopupWindow)
            listPopupWindow.show()
        }
    }

    private fun setPopupItemClickListener(viewId: Int, popupMenu: ListPopupWindow) {
        val textView: TextView = when (viewId) {
            binding.cvCommunityAddpostRecommendGender.id -> binding.tvCommunityAddpostRecommendGender
            binding.cvCommunityAddpostClothesCategory.id -> binding.tvCommunityAddpostClothesCategory
            binding.cvCommuntiyAddpostSize.id -> binding.tvCommuntiyAddpostSize
            binding.cvCommunityAddpostTransactionMethod.id -> binding.tvCommunityAddpostTransactionMethod
            else -> return
        }

        popupMenu.setOnItemClickListener { _, view, _, _ ->
            val itemDescription = (view as TextView).text.toString()
            textView.text = itemDescription
            Timber.d(itemDescription)
            communityAddPostViewModel.selectTransactionMethod(itemDescription)
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
            R.style.ListPopupMenuWindow_CommunityAddPostOption,
            R.layout.list_popup_window_item_window_dark,
            items
        )
    }


    private fun selectTransactionType() {
        binding.cgCommunityAddpostMethod.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.size > 0) {
                val checkedMethodType = binding.root.findViewById<Chip>(checkedIds[0])

                communityAddPostViewModel.checkTransactionType(
                    checkedMethodType.text.toString(),
                    resources.getStringArray(R.array.community_item_search_option_type).toList()
                )
            }

        }
    }


}