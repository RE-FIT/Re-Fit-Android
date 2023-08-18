package com.example.refit.presentation.mypage

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.viewpager2.widget.ViewPager2
import com.example.refit.MainActivity
import com.example.refit.R
import com.example.refit.databinding.FragmentMyInfoBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DialogUtil.createAlertBasicDialog
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.dialog.AlertBasicDialogListener
import com.example.refit.presentation.findidpassword.adapter.FragmentPageAdapter
import com.example.refit.presentation.mypage.viewmodel.MyInfoViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MyInfoFragment : BaseFragment<FragmentMyInfoBinding>(R.layout.fragment_my_info) {

    private lateinit var viewPager : ViewPager2
    private lateinit var tabLayout : TabLayout


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        connectionTabLayout()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)


        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    createAlertBasicDialog(
                        resources.getString(R.string.pw_change_delete_title),
                        resources.getString(R.string.pw_change_delete_positive),
                        resources.getString(R.string.pw_change_delete_negative),
                        object : AlertBasicDialogListener {
                            override fun onClickPositive() {
                                navigate(R.id.action_myInfo_to_nav_my_page)
                            }

                            override fun onClickNegative() {
                            }
                        }).show(requireActivity().supportFragmentManager, null)
                }
            })
    }

    fun connectionTabLayout() {

        val tabTitle = arrayOf("회원정보 수정", "비밀번호 수정")

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

}