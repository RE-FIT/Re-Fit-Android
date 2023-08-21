package com.example.refit.presentation.closet

import android.os.Bundle
import android.view.View
import com.example.refit.R
import com.example.refit.databinding.FragmentCompletedForestBinding
import com.example.refit.presentation.closet.viewmodel.ForestViewModel
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigateUp
import com.example.refit.presentation.common.WindowUtil.setStatusBarColor
import com.example.refit.presentation.mypage.viewmodel.MyInfoViewModel
import com.example.refit.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class CompletedForestFragment : BaseFragment<FragmentCompletedForestBinding>(R.layout.fragment_completed_forest) {

    private val forestViewModel: ForestViewModel by activityViewModel()
    private val myInfoViewModel: MyInfoViewModel by activityViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.forest_sky_blue)
        binding.forestViewModel = forestViewModel
        myInfoViewModel.showMyInfoRetrofit()
        forestViewModel.getRegisteredClothInfo()

        handleGetMyInfo()
        handleGetClothInfo()
        handleCloseButton()
    }

    private fun handleGetMyInfo() {
        myInfoViewModel.myInfoResponse.observe(viewLifecycleOwner) {
            binding.myInfo = it
        }
    }

    private fun handleGetClothInfo() {
        forestViewModel.clothItemInfo.observe(viewLifecycleOwner, EventObserver {
            binding.clothInfo = it
        })
    }

    private fun handleCloseButton() {
        binding.tvCompletedForestCloseButton.setOnClickListener {
            navigateUp()
        }
    }

}