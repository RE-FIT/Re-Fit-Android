package com.example.refit.presentation.mypage

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyInfoViewpagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    // 페이지 갯수 설정

    val fragmentList : MutableList<Fragment> = arrayListOf()

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun addFragment(fragment: Fragment){
        fragmentList.add(fragment)
        notifyItemInserted(fragmentList.size - 1)
    }

    fun removeFragment(){
        fragmentList.removeLast()
        notifyItemRemoved(fragmentList.size)
    }
}