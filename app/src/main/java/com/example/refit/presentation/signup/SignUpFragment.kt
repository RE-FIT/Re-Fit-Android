package com.example.refit.presentation.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import com.example.refit.R
import com.example.refit.databinding.FragmentSignUpBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.dialog.signup.SignUpAgreeDialog
import com.example.refit.presentation.signin.viewmodel.SignInViewModel
import com.example.refit.presentation.signup.viewmodel.SignUpViewModel
import com.example.refit.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlin.math.sign

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    private val signUpViewModel: SignUpViewModel by activityViewModel()
    private val viewModel: SignInViewModel by sharedViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = signUpViewModel
        handleInputId()
        handleIdEvent()
        handleInputPassword()
        handlePasswordEvent()
        handleInputEmail()
        handleEmailFormatEvent()
        handleEmailCodeCertification()
        handleEmailCodeFormatEvent()
        handleEmailCodeEvent()
        handleEmailEvent()
        handleInputNickname()
        handleNicknameFormatEvent()
        handleNicknameEvent()
        handleBirthDate()
        handleBirthEvent()
        initSexDropDownMenu()
        handleSexEvent()
        handleSignUpAgreeCheckBox()
        handleAgreeEvent()
        handleClickSignUpButton()
        handleCompleteSignUp()
        handleClickCloseButton()
    }

    private fun handleClickCloseButton() {
        binding.tvSignUpClose.setOnClickListener {
            navigate(R.id.action_signUpFragment_to_signInFragment)
        }
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
                    "^\\S+\$".toRegex(),
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
                    "^(?:\\d{4}/\\d{2}/\\d{2}|\\d{8})\$".toRegex(),
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
            signUpViewModel.checkValidationAgree(checkBox.isChecked)
        }
        binding.etSignUpRequestAgreeDescription.setOnClickListener {
            SignUpAgreeDialog().show(requireActivity().supportFragmentManager, null)
        }
    }

    private fun handleClickSignUpButton() {
        binding.btnSignUpComplete.setOnClickListener {
            signUpViewModel.signUpUser(
                binding.etSignUpInputId.text.toString(),
                binding.etSignUpInputPassword.text.toString(),
                binding.etSignUpInputEmail.text.toString(),
                binding.etSignUpInputNickname.text.toString(),
                binding.etSignUpInputBirth.text.toString(),
                resources.getStringArray(R.array.sign_up_gender).toList()
                    .indexOf(binding.atvSignUpDropDownSex.text.toString())
            )
        }
    }

    private fun handleCompleteSignUp() {
        signUpViewModel.isSuccessSignUp.observe(viewLifecycleOwner, EventObserver{isSuccess ->
            if(isSuccess) {
                navigate(R.id.action_signUpFragment_to_signUpCompleteFragment)
            }
        })
    }

    private fun handleIdEvent() {
        signUpViewModel.isValidId.observe(viewLifecycleOwner, EventObserver {isValid ->
            binding.isValidId = isValid
        })
    }

    private fun handlePasswordEvent() {
        signUpViewModel.isValidPassword.observe(viewLifecycleOwner, EventObserver {isValid ->
            binding.isValidPassword = isValid
        })
    }

    private fun handleEmailFormatEvent() {
        signUpViewModel.isValidEmailFormat.observe(viewLifecycleOwner, EventObserver {isValid ->
            binding.isValidEmailFormat = isValid
        })
    }

    private fun handleEmailCodeFormatEvent() {
        signUpViewModel.isValidEmailCodeFormat.observe(viewLifecycleOwner, EventObserver {isValid ->
            binding.isValidEmailCodeFormat = isValid
        })
    }

    private fun handleEmailCodeEvent() {
        signUpViewModel.emailCode.observe(viewLifecycleOwner, EventObserver {
            binding.emailCode = it
        })
    }

    private fun handleEmailEvent() {
        signUpViewModel.isValidEmail.observe(viewLifecycleOwner, EventObserver {isValid ->
            binding.isValidEmail = isValid
        })
    }

    private fun handleNicknameFormatEvent() {
        signUpViewModel.isValidNicknameFormat.observe(viewLifecycleOwner, EventObserver {isValid ->
            binding.isValidNicknameFormat = isValid
        })
    }

    private fun handleNicknameEvent() {
        signUpViewModel.isValidNickname.observe(viewLifecycleOwner, EventObserver {isValid ->
            binding.isValidNickname = isValid
        })
    }

    private fun handleBirthEvent() {
        signUpViewModel.isValidBirt.observe(viewLifecycleOwner, EventObserver {isValid ->
            binding.isValidBirth = isValid
        })
    }

    private fun handleSexEvent() {
        signUpViewModel.isValidSex.observe(viewLifecycleOwner, EventObserver {isValid ->
            binding.isValidSex = isValid
        })
    }

    private  fun handleAgreeEvent() {
        signUpViewModel.isValidAgree.observe(viewLifecycleOwner, EventObserver {isValid ->
            binding.isValidAgree = isValid
        })
    }


}