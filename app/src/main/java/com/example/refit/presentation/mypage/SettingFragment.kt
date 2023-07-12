package com.example.refit.presentation.mypage

import android.os.Bundle
import android.view.View
import com.example.refit.R
import com.example.refit.databinding.FragmentSettingBinding
import com.example.refit.presentation.common.BaseFragment

class SettingFragment: BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {

        }


    }

}