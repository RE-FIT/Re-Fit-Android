package com.example.refit.presentation.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.refit.R
import com.example.refit.databinding.FragmentStartingBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigate


class StartingFragment : BaseFragment<FragmentStartingBinding>(R.layout.fragment_starting) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStartingBottom.setOnClickListener {
            navigate(R.id.action_startingFragment_to_signInFragment)
        }

        binding.btnStartingTop.setOnClickListener {

        }

        binding.tvStartingFindIdPassword.setOnClickListener {
            navigate(R.id.action_startingFragment_to_findIdPasswordFragment)
        }

        binding.tvStartingBasicSignUp.setOnClickListener {
            navigate(R.id.action_startingFragment_to_signUpFragment)
        }
    }


}