package com.example.refit.presentation.onboarding

import android.os.Bundle
import android.view.View
import com.example.refit.R
import com.example.refit.databinding.FragmentStartingBinding
import com.example.refit.presentation.AccessTokenViewModel
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class StartingFragment : BaseFragment<FragmentStartingBinding>(R.layout.fragment_starting) {

    private val tokenViewModel: AccessTokenViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tokenViewModel.checkAccessToken()

        //토큰 체크 후, 만약 엑세스 토큰이 유효하다면 이동
        tokenViewModel.success.observe(viewLifecycleOwner, EventObserver {
            navigate(R.id.action_startingFragment_to_nav_closet)
        })

        binding.btnStartingBottom.setOnClickListener {
            navigate(R.id.action_startingFragment_to_signInFragment)
        }

        binding.btnStartingTop.setOnClickListener {
            navigate(R.id.action_startingFragment_to_introContainerFragment)
        }

        binding.tvStartingFindIdPassword.setOnClickListener {
            navigate(R.id.action_startingFragment_to_findIdPasswordFragment)
        }

        binding.tvStartingBasicSignUp.setOnClickListener {
            navigate(R.id.action_startingFragment_to_signUpFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        tokenViewModel.checkAccessToken()
    }
}