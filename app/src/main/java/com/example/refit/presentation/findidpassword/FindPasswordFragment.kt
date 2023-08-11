package com.example.refit.presentation.findidpassword

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.example.refit.R
import com.example.refit.databinding.FragmentFindPasswordBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.findidpassword.viewModel.FindIdPasswordViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FindPasswordFragment : BaseFragment<FragmentFindPasswordBinding>(R.layout.fragment_find_password) {

    private val vm: FindIdPasswordViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editNickname()
        editEmail()
        editId()
    }

    private fun editNickname() {
        // 이름(닉네임)
        binding.findIdEditName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                vm.setFindPwFilledStatus(0, true, s.toString())
            }
            override fun afterTextChanged(s: Editable?) { vm.setFindPwAllFilledStatus() }
        })
    }

    private fun editEmail() {
        // 이메일
        binding.findIdEditEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                vm.setFindPwFilledStatus(1, true, s.toString())
            }
            override fun afterTextChanged(s: Editable?) { vm.setFindPwAllFilledStatus() }
        })
    }

    private fun editId() {
        // 아이디
        binding.findPwEditId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                vm.setFindPwFilledStatus(2, true, s.toString())
            }
            override fun afterTextChanged(s: Editable?) { vm.setFindPwAllFilledStatus() }
        })
    }

    private fun clickFindPwBtn() {
        binding.btnFindPw.setOnClickListener {

        }
    }

}