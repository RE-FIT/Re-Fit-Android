package com.example.refit.presentation.onboarding.pages

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnBoardingAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = 5

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> OnBoardingFirstFragment()
            1 -> OnBoardingSecondFragment()
            2 -> OnBoardingThirdFragment()
            3 -> OnBoardingFourthFragment()
            4 -> OnBoardingFifthFragment()
            else -> throw IndexOutOfBoundsException()
        }
    }

}