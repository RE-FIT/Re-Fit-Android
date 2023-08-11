package com.example.refit.presentation.findidpassword.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.refit.presentation.findidpassword.FindIdFragment
import com.example.refit.presentation.findidpassword.FindPasswordFragment

class FragmentPageAdapter(fragment: Fragment): FragmentStateAdapter(fragment) /*(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle)*/{
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