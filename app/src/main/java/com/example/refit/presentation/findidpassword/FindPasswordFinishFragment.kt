package com.example.refit.presentation.findidpassword

import android.os.Bundle
import android.view.View
import com.example.refit.R
import com.example.refit.databinding.FragmentFindPasswordBinding
import com.example.refit.databinding.FragmentFindPasswordFinishBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigate

class FindPasswordFinishFragment : BaseFragment<FragmentFindPasswordFinishBinding>(R.layout.fragment_find_password_finish) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.findPasswordBtn.setOnClickListener(){
            navigate(R.id.action_findPasswordFinishFragment_to_signInFragment)
        }

    }
}