package com.example.refit.presentation.signup

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import com.example.refit.R
import com.example.refit.databinding.FragmentSignUpBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.dialog.signup.SignUpAgreeDialog
import com.example.refit.presentation.signin.viewmodel.SignInViewModel
import com.example.refit.presentation.signup.viewmodel.SignUpViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import timber.log.Timber
import java.util.regex.Pattern
import kotlin.math.sign

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    private val signUpViewModel: SignUpViewModel by sharedViewModel()
    private val viewModel: SignInViewModel by sharedViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = signUpViewModel
        handleInputId()
        handleInputPassword()
        handleInputEmail()
        handleEmailCodeCertification()
        handleInputNickname()
        handleBirthDate()
        initSexDropDownMenu()
        handleSignUpAgreeCheckBox()
    }

    private fun handleInputId() {
        binding.etSignUpInputId.addTextChangedListener(object :
            SignUpInputTextWatcher(binding.tlSignUpInputId) {
            override fun afterTextChanged(text: Editable?) {
                super.afterTextChanged(text)
                signUpViewModel.checkValidationId(
                    "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{8,16}\$".toRegex(), text.toString()
                )
            }
        })
    }

    private fun handleInputPassword() {
        binding.etSignUpInputPassword.addTextChangedListener(object :
            SignUpInputTextWatcher(binding.tlSignUpInputPassword) {
            override fun afterTextChanged(text: Editable?) {
                super.afterTextChanged(text)
                signUpViewModel.checkValidationPassword(
                    "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!_\\-])[a-zA-Z\\d!_\\-]{8,16}\$".toRegex(),
                    text.toString()
                )
            }
        })
    }

    private fun handleInputEmail() {
        binding.etSignUpInputEmail.addTextChangedListener(object :
            SignUpInputTextWatcher(binding.tlSignUpInputEmail) {
            override fun afterTextChanged(text: Editable?) {
                super.afterTextChanged(text)
                signUpViewModel.checkValidationEmailFormat(
                    "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$".toRegex(),
                    text.toString()
                )
            }
        })
    }

    private fun handleEmailCodeCertification() {
        binding.etSignUpInputEmailCertificationCode.addTextChangedListener(object :
            SignUpInputTextWatcher(binding.tlSignUpInputEmailCertificationCode) {
            override fun afterTextChanged(text: Editable?) {
                super.afterTextChanged(text)
                signUpViewModel.checkValidationEmailCodeFormat(
                    "^\\d+\$".toRegex(),
                    text.toString()
                )
            }
        })
    }

    private fun handleInputNickname() {
        binding.etSignUpInputNickname.addTextChangedListener(object :
            SignUpInputTextWatcher(binding.tlSignUpInputNickname) {
            override fun afterTextChanged(text: Editable?) {
                super.afterTextChanged(text)
                signUpViewModel.checkValidationNickname(
                    "^[A-Za-z가-힣]+\$".toRegex(),
                    text.toString()
                )

            }
        })
    }

    private fun handleBirthDate() {
        binding.etSignUpInputBirth.addTextChangedListener(object :
            SignUpInputTextWatcher(binding.tlSignUpInputBirth) {
            override fun afterTextChanged(text: Editable?) {
                super.afterTextChanged(text)
                signUpViewModel.checkValidationBirth(
                    "^(?:19|20)\\d\\d/(?:0[1-9]|1[0-2])/(?:0[1-9]|[12][0-9]|3[01])\$".toRegex(),
                    text.toString()
                )
            }
        })
    }

    private fun initSexDropDownMenu() {
        val items = resources.getStringArray(R.array.sign_up_gender).toList()
        val adapter =
            ArrayAdapter(requireContext(), R.layout.list_popup_window_item_window_dark, items)
        binding.atvSignUpDropDownSex.setAdapter(adapter)
        binding.atvSignUpDropDownSex.setDropDownBackgroundResource(R.drawable.bg_solid_white_radius_10)
        binding.atvSignUpDropDownSex.addTextChangedListener {
            signUpViewModel.checkValidationSex(true)
        }
    }

    private fun handleSignUpAgreeCheckBox() {
        binding.cbSignUpRequestAgree.addOnCheckedStateChangedListener { checkBox, state ->
            if (checkBox.isChecked) {
            }
        }

        binding.etSignUpRequestAgreeDescription.setOnClickListener {
            SignUpAgreeDialog().show(requireActivity().supportFragmentManager, null)
        }
    }

    abstract inner class SignUpInputTextWatcher(
        private val containerView: TextInputLayout
    ) : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(text: Editable?) {
            text?.let { inputText ->
                if (inputText.isEmpty()) {
                    containerView.error = null
                    containerView.boxStrokeWidth = 0
                    containerView.helperText = null
                }
            }
        }
    }
}