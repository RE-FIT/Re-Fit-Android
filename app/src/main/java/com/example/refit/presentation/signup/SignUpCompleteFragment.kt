package com.example.refit.presentation.signup

import android.os.Bundle
import android.view.View
import com.example.refit.R
import com.example.refit.databinding.FragmentSignUpCompleteBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.signup.viewmodel.SignUpViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SignUpCompleteFragment : BaseFragment<FragmentSignUpCompleteBinding>(R.layout.fragment_sign_up_complete) {

    private val signUpViewModel: SignUpViewModel by activityViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = signUpViewModel

        binding.btnSignUpCompleteToSignIn.setOnClickListener {
            navigate(R.id.action_signUpCompleteFragment_to_signInFragment)
        }
    }
}