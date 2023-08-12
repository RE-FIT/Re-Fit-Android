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
        handleBirthDate()
        initSexDropDownMenu()
        handleSignUpAgreeCheckBox()
    }

    private fun handleInputId() {
        binding.etSignUpInputId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(text: Editable?) {
                text?.let {
                    val regex = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{8,16}\$".toRegex()
                    if (it.isEmpty()) {
                        binding.tlSignUpInputId.error = null
                        binding.tlSignUpInputId.boxStrokeWidth = 0
                        binding.tlSignUpInputId.helperText = null
                    } else if (!regex.matches(it)) {
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
        binding.etSignUpInputPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(text: Editable?) {
                text?.let {
                    val regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!_\\-])[a-zA-Z\\d!_\\-]{8,16}\$".toRegex()
                    if (it.isEmpty()) {
                        binding.tlSignUpInputPassword.error = null
                        binding.tlSignUpInputPassword.boxStrokeWidth = 0
                        binding.tlSignUpInputPassword.helperText = null
                    } else if (!regex.matches(it)) {
                        binding.tlSignUpInputPassword.error = "* 영문 대문자, 소문자, 숫자, 특수기호 !, -, _ 중 모두 포함하며 8글자 이상 16글자 이하여야 합니다"
                        binding.tlSignUpInputPassword.boxStrokeWidth = 4
                    } else {
                        binding.tlSignUpInputPassword.error = null
                        binding.tlSignUpInputPassword.boxStrokeWidth = 4
                        binding.tlSignUpInputPassword.helperText = "맞는데용?"
                    }
                }
            }
        })
    }

    private fun handleInputEmail() {
        binding.etSignUpInputEmail.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(text: Editable?) {
                text?.let {
                    val emailValidation = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$".toRegex()
                    if (it.isEmpty()) {
                        binding.tlSignUpInputEmail.error = null
                        binding.tlSignUpInputEmail.boxStrokeWidth = 0
                        binding.tlSignUpInputEmail.helperText = null
                    } else if (!emailValidation.matches(it)) {
                        binding.btnSignUpEmailCertification.setBackgroundColor(resources.getColor(R.color.light, null))
                        binding.btnSignUpEmailCertification.setTextColor(resources.getColor(R.color.black, null))
                        binding.tlSignUpInputEmail.error = "* 이메일 양식을 정확하게 입력해주세요"
                        binding.tlSignUpInputEmail.boxStrokeWidth = 4
                    } else {
                        binding.btnSignUpEmailCertification.setBackgroundColor(resources.getColor(R.color.green1, null))
                        binding.btnSignUpEmailCertification.setTextColor(resources.getColor(R.color.white, null))
                        binding.tlSignUpInputEmail.error = null
                        binding.tlSignUpInputEmail.boxStrokeWidth = 4
                        binding.tlSignUpInputEmail.helperText = "맞는데용?"
                    }
                }
            }
        })
    }

    private fun handleEmailCodeCertification() {
        binding.etSignUpInputEmailCertificationCode.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(text: Editable?) {
                text?.let {
                    if(it.isEmpty()) {
                        binding.tlSignUpInputEmailCertificationCode.error = null
                        binding.tlSignUpInputEmailCertificationCode.boxStrokeWidth = 0
                        binding.tlSignUpInputEmailCertificationCode.helperText = null
                        binding.btnSignUpEmailCodeCertification.setBackgroundColor(resources.getColor(R.color.light, null))
                        binding.btnSignUpEmailCodeCertification.setTextColor(resources.getColor(R.color.black, null))
                    } else {
                        binding.btnSignUpEmailCodeCertification.setBackgroundColor(resources.getColor(R.color.green1, null))
                        binding.btnSignUpEmailCodeCertification.setTextColor(resources.getColor(R.color.white, null))
                        binding.tlSignUpInputEmailCertificationCode.boxStrokeWidth = 4
                    }
                }
            }
        })

        signUpViewModel.isValidEmail.observe(viewLifecycleOwner) {isValid ->
            if(isValid) {
                binding.tlSignUpInputEmailCertificationCode.helperText = "올바른 인증번호입니다"
            } else {
                binding.tlSignUpInputEmailCertificationCode.error = "인증번호를 다시 한 번 확인해주세요"
                binding.tlSignUpInputEmailCertificationCode.helperText = null
            }
        }
    }

    private fun handleBirthDate() {
        binding.etSignUpInputBirth.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(text: Editable?) {
                text?.let {
                    val regex = "^(?:19|20)\\d\\d/(?:0[1-9]|1[0-2])/(?:0[1-9]|[12][0-9]|3[01])\$".toRegex()
                    if(it.isEmpty()) {
                        binding.tlSignUpInputBirth.error = null
                        binding.tlSignUpInputBirth.boxStrokeWidth = 0
                        binding.tlSignUpInputBirth.helperText = null
                    } else if(!regex.matches(it)) {
                        binding.tlSignUpInputBirth.error = "* YYYY/MM/DD 형식으로 작성해야 합니다."
                        binding.tlSignUpInputBirth.boxStrokeWidth = 4
                    } else {
                        binding.tlSignUpInputBirth.error = null
                        binding.tlSignUpInputBirth.boxStrokeWidth = 4
                    }
                }
            }
        })
    }

    private fun initSexDropDownMenu() {
        val items = resources.getStringArray(R.array.sign_up_gender).toList()
        val adapter = ArrayAdapter(requireContext(), R.layout.list_popup_window_item_window_dark, items)
        binding.atvSignUpDropDownSex.setAdapter(adapter)
        binding.atvSignUpDropDownSex.setDropDownBackgroundResource(R.drawable.bg_solid_white_radius_10)
        binding.atvSignUpDropDownSex.addTextChangedListener {
            binding.atvSignUpDropDownSex.setTextColor(resources.getColor(R.color.green1, null))
            binding.tlSignUpDropDownSex.setEndIconTintList(resources.getColorStateList(R.color.selector_selected_dark2_else_green1, null))
            binding.tlSignUpDropDownSex.boxStrokeWidth = 4
        }
    }

    private fun handleSignUpAgreeCheckBox() {
        binding.cbSignUpRequestAgree.addOnCheckedStateChangedListener { checkBox, state ->
            if(checkBox.isChecked) {
            }
        }

        binding.etSignUpRequestAgreeDescription.setOnClickListener {
            SignUpAgreeDialog().show(requireActivity().supportFragmentManager, null)
        }
    }

}