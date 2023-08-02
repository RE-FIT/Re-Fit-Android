package com.example.refit.presentation.findidpassword

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.refit.R
import com.example.refit.databinding.FragmentFindIdPasswordBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.findidpassword.adapter.FragmentPageAdapter
import com.google.android.material.tabs.TabLayoutMediator


class FindIdPasswordFragment : BaseFragment<FragmentFindIdPasswordBinding>(R.layout.fragment_find_id_password) {

    private lateinit var fragmentPageAdapter: FragmentPageAdapter
    private lateinit var viewPager: ViewPager2


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragmentPageAdapter = FragmentPageAdapter(this)
        viewPager = binding.viewPager2
        viewPager.adapter = fragmentPageAdapter

        TabLayoutMediator(binding.tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "아이디찾기"
                1 -> tab.text = "비밀번호찾기"
            }
        }.attach()


        binding.back.setOnClickListener {
            navigate(R.id.action_findIdPasswordFragment_to_signInFragment)
        }
    }
}