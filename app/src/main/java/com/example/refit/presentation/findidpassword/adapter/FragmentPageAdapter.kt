package com.example.refit.presentation.findidpassword.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.refit.presentation.findidpassword.FindIdFragment
import com.example.refit.presentation.findidpassword.FindPasswordFragment

class FragmentPageAdapter(fragment: Fragment): FragmentStateAdapter(fragment) /*(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle)*/{
    /*override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if(position == 0)
            FindIdFragment()
        else
            FindPasswordFragment()
    }*/

    override fun getItemCount(): Int = 2


    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> FindIdFragment()
            else -> FindPasswordFragment()
        }
    }

}