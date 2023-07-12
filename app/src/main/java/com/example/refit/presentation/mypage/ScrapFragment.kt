package com.example.refit.presentation.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.refit.R
import com.example.refit.databinding.FragmentMyInfoBinding
import com.example.refit.databinding.FragmentScrapBinding
import com.example.refit.presentation.common.BaseFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ScrapFragment: BaseFragment<FragmentScrapBinding>(R.layout.fragment_scrap) {
    private lateinit var viewPager : ViewPager2
    private lateinit var tabLayout : TabLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabTitle = arrayOf("판매", "나눔")

        viewPager = binding.viewPager // viewPager 연결
        tabLayout = binding.tabLayout // tabLayout 연결

        var adapter = MyInfoViewpagerAdapter(this)

        adapter.addFragment(ScrapSaleFragment())
        adapter.addFragment(ScrapShareFragment())

        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager
        ) { tab, position -> tab.text = tabTitle[position] }.attach()
    }

}