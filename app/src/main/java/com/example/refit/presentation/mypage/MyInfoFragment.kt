package com.example.refit.presentation.mypage

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.refit.R
import com.example.refit.databinding.FragmentMyInfoBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DialogUtil.checkPwFailDialog
import com.example.refit.presentation.findidpassword.adapter.FragmentPageAdapter
import com.example.refit.presentation.mypage.viewmodel.MyInfoViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MyInfoFragment : BaseFragment<FragmentMyInfoBinding>(R.layout.fragment_my_info) {

    private lateinit var viewPager : ViewPager2
    private lateinit var tabLayout : TabLayout

    private val vm: MyInfoViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        connectionTabLayout()
        blockPasswordChangeTab()
    }

    fun connectionTabLayout() {

        val tabTitle = arrayOf("회원정보 수정", "비밀번호 변경")

        viewPager = binding.viewPager // viewPager 연결
        tabLayout = binding.tabLayout // tabLayout 연결

        val adapter = FragmentPageAdapter(this)

        adapter.addFragment(MyInfoUpdateFragment())
        adapter.addFragment(MyInfoPwUpdateFragment())

        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager
        ) { tab, position -> tab.text = tabTitle[position] }.attach()
    }

    fun blockPasswordChangeTab() {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 1 && vm.type.value != null) {
                    viewPager.setCurrentItem(0, false)
                    notifyBlockChangeDialog()
                }
            }
        })
    }

    private fun notifyBlockChangeDialog() {
        checkPwFailDialog(
            resources.getString(R.string.block_change_to_pw_title),
            resources.getString(R.string.block_change_to_pw_content)
        ).show(requireActivity().supportFragmentManager, null)
    }
}