package com.example.refit.presentation.mypage

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.refit.R
import com.example.refit.databinding.FragmentMyFeedBinding
import com.example.refit.databinding.FragmentMyInfoBinding
import com.example.refit.presentation.common.BaseFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MyFeedFragment : BaseFragment<FragmentMyFeedBinding>(R.layout.fragment_my_feed) {
    private lateinit var viewPager : ViewPager2
    private lateinit var tabLayout : TabLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabTitle = arrayOf("판매", "나눔", "구매")

        viewPager = binding.viewPager // viewPager 연결
        tabLayout = binding.tabLayout // tabLayout 연결

        var adapter = MyInfoViewpagerAdapter(this@MyFeedFragment)

        adapter.addFragment(MyFeedSaleFragment())
        adapter.addFragment(MyFeedShareFragment())
        adapter.addFragment(MyFeedPurchaseFragment())

        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager
        ) { tab, position -> tab.text = tabTitle[position] }.attach()
    }

}