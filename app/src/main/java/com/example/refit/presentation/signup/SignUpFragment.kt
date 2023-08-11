package com.example.refit.presentation.signup

import android.graphics.Color
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
import timber.log.Timber
import java.util.regex.Pattern

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    private val signUpViewModel: SignUpViewModel by sharedViewModel()
    private val viewModel: SignInViewModel by sharedViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleInputId()
    }

    private fun handleInputId() {
        binding.etSignUpInputId.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                p0?.let{
                    val regex = "^(?=.*[a-zA-Z])(?=.*\\d).{8,16}$".toRegex()
                    if(it.isEmpty()) {
                        binding.tlSignUpInputId.error = null
                        binding.tlSignUpInputId.boxStrokeWidth = 0
                        binding.tlSignUpInputId.helperText = null
                    } else if(!regex.matches(it)) {
                        binding.tlSignUpInputId.error = "* 8-16자의 영문, 숫자를 포함해야 합니다"
                        binding.tlSignUpInputId.boxStrokeWidth = 4
                    } else {
                        binding.tlSignUpInputId.error = null
                        binding.tlSignUpInputId.boxStrokeWidth = 4
                        binding.tlSignUpInputId.helperText = "맞는데용?"
                    }
                }
            }
        })
    }

    private fun handleInputPassword() {

    }

    private fun handleInputEmail() {

    }


        private fun handleRequestEmailCertification() {
            signUpViewModel.emailCode.observe(viewLifecycleOwner) { emailCertificationResponse ->
                binding.emailCertificationCode = emailCertificationResponse
            }
        }
}