package com.example.refit.presentation.findidpassword

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.example.refit.R
import com.example.refit.databinding.FragmentFindIdPasswordBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.findidpassword.adapter.FragmentPageAdapter
import com.example.refit.presentation.findidpassword.viewModel.FindIdPasswordViewModel
import com.example.refit.presentation.mypage.MyInfoPwUpdateFragment
import com.example.refit.presentation.mypage.MyInfoUpdateFragment
import com.example.refit.presentation.mypage.adapter.MyInfoViewpagerAdapter
import com.example.refit.presentation.mypage.viewmodel.MyScrapViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class FindIdPasswordFragment : BaseFragment<FragmentFindIdPasswordBinding>(R.layout.fragment_find_id_password) {

    private val vm: FindIdPasswordViewModel by sharedViewModel()

    private lateinit var viewPager : ViewPager2
    private lateinit var tabLayout : TabLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = vm

        connectionTabLayout()
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.initFilledState()
    }

    fun connectionTabLayout() {

        val tabTitle = arrayOf("아이디 찾기", "비밀번호 찾기")

        viewPager = binding.viewPager // viewPager 연결
        tabLayout = binding.tabLayout // tabLayout 연결

        val adapter = FragmentPageAdapter(this)
        adapter.addFragment(FindIdFragment())
        adapter.addFragment(FindPasswordFragment())

        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager
        ) { tab, position -> tab.text = tabTitle[position] }.attach()
    }
}