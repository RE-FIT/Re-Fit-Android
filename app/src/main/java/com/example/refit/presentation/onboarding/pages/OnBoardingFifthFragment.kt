package com.example.refit.presentation.onboarding.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.refit.R
import com.example.refit.databinding.FragmentOnBoardingFifthBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigate

class OnBoardingFifthFragment : BaseFragment<FragmentOnBoardingFifthBinding>(R.layout.fragment_on_boarding_fifth) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnOnBoardingToSignIn.setOnClickListener {
            navigate(R.id.action_introContainerFragment_to_signInFragment)
        }
    }
}