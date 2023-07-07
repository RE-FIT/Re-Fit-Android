package com.example.refit.presentation.mypage

import android.os.Bundle
import android.view.View
import com.example.refit.R
import com.example.refit.databinding.FragmentMyInfoBinding
import com.example.refit.presentation.common.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator

class MyInfoFragment : BaseFragment<FragmentMyInfoBinding>(R.layout.fragment_my_info) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = binding.myInfoViewPager
        val tabLayout = binding.myInfoTabLayout

        val viewPagerFragmentAdapter = MyInfoViewpagerAdapter(this@MyInfoFragment)
        viewPager.adapter = viewPagerFragmentAdapter

        val tabTitle = listOf<String>("회원정보 수정", "비밀번호 변경")

        TabLayoutMediator(tabLayout, viewPager
        ) { tab, position -> tab.text = tabTitle[position] }.attach()

    }

}