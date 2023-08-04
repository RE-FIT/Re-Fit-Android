package com.example.refit.presentation.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.example.refit.R
import com.example.refit.databinding.FragmentIntroConatinerBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.onboarding.pages.OnBoardingAdapter
import com.google.android.material.tabs.TabLayoutMediator

class IntroContainerFragment : BaseFragment<FragmentIntroConatinerBinding>(R.layout.fragment_intro_conatiner) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnBoardingPager(requireActivity())
        setIndicatorTabMediator()
        handleViewPagerEvent()
    }

    private fun setOnBoardingPager(fragmentActivity: FragmentActivity) {
        binding.vpOnBoardingPage.adapter = OnBoardingAdapter(fragmentActivity)
    }

    private fun setIndicatorTabMediator() {
        TabLayoutMediator(binding.tlIntroPageIndicator, binding.vpOnBoardingPage) { _, _ -> }.attach()
    }

    private fun handleViewPagerEvent() {
        binding.tvIntroSkip.setOnClickListener {
            binding.vpOnBoardingPage.adapter?.let {
                binding.vpOnBoardingPage.currentItem = it.itemCount - 1

            }
        }
    }

}