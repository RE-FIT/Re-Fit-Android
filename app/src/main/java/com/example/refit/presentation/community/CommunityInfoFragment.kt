package com.example.refit.presentation.community

import android.os.Bundle

import android.view.View
import com.example.refit.R
import com.example.refit.databinding.FragmentCommunityInfoBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.CustomSnackBar
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.community.viewmodel.CommunityAddPostViewModel
import com.example.refit.presentation.community.viewmodel.CommunityInfoViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CommunityInfoFragment : BaseFragment<FragmentCommunityInfoBinding>(R.layout.fragment_community_info) {

    private val communityInfoViewModel: CommunityInfoViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = communityInfoViewModel
        handleFavIconClicked()

    }

    private fun handleFavIconClicked() {
        binding.tbCommunityInfoFav.setOnClickListener {
            val toggleStatus = !binding.tbCommunityInfoFav.isChecked
            if (toggleStatus) {
                CustomSnackBar.make(binding.clCommunityInfo, "스크랩을 완료하였습니다!", R.anim.anim_show_snack_bar_from_bottom).show()
            }
            communityInfoViewModel.setScrapState(toggleStatus)
        }

        // 임시 연결 코드
        binding.fabCommunityInfoChat.setOnClickListener {
            navigate(R.id.action_communityInfoFragment_to_postReportFragment)
        }
    }

}