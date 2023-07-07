package com.example.refit.presentation.mypage

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyInfoViewpagerAdapter(fragment: MyInfoFragment): FragmentStateAdapter(fragment) {
    val list = listOf<Fragment>(MyInfoUpdateFragment(), MyInfoPwUpdateFragment())

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }

}