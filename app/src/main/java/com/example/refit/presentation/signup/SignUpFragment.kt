package com.example.refit.presentation.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.refit.R
import com.example.refit.databinding.FragmentSignUpBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.signin.viewmodel.SignInViewModel
import com.example.refit.presentation.signup.viewmodel.SignUpViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import java.util.regex.Pattern

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    private val signUpViewModel: SignUpViewModel by sharedViewModel()
    private val viewModel: SignInViewModel by sharedViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleRequestEmailCertification()

        binding.btnSignUpApiTest.setOnClickListener {
            signUpViewModel.certificateEmail("yoonmin0113@naver.com")
        }

        binding.signUp.setOnClickListener {
            navigate(R.id.action_signUpFragment_to_communityFragment)
        }

        binding.signUpBack.setOnClickListener{
            navigate(R.id.action_signUpFragment_to_signInFragment)
        }
    }

        private fun handleRequestEmailCertification() {
            signUpViewModel.emailCode.observe(viewLifecycleOwner) { emailCertificationResponse ->
                binding.emailCertificationCode = emailCertificationResponse
            }
        }

}