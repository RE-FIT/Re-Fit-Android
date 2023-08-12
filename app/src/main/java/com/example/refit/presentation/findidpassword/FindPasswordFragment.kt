package com.example.refit.presentation.findidpassword

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import com.example.refit.R
import com.example.refit.data.model.signup.FindIdRequest
import com.example.refit.data.model.signup.FindPasswordRequest
import com.example.refit.databinding.FragmentFindPasswordBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.CustomSnackBar
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.findidpassword.viewModel.FindIdPasswordViewModel
import com.example.refit.presentation.findidpassword.viewModel.FindIdPwViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FindPasswordFragment : BaseFragment<FragmentFindPasswordBinding>(R.layout.fragment_find_password) {

    private val vm: FindIdPasswordViewModel by sharedViewModel()
    private val viewModel: FindIdPwViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.error.observe(viewLifecycleOwner) {
            it?.let {
                binding.btnFindPw.setOnClickListener(){
                    val name = binding.findIdEditName.text.toString()
                    val id = binding.findPwEditId.text.toString()
                    val email = binding.findIdEditEmail.text.toString()
                    val findPasswordRequest = FindPasswordRequest(name, id, email)

                    viewModel.findByPassword(findPasswordRequest)
                }
            }
        }

        //아이디 찾기 성공 시 이동
        viewModel.idSuccess.observe(viewLifecycleOwner, Observer {
            navigate(R.id.action_findIdPasswordFragment_to_findPasswordFinishFragment)
        })

        editNickname()
        editEmail()
        editId()
    }

    //비밀번호 찾기 실패 시 snackBar
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.error.observe(viewLifecycleOwner) {
            it?.let {
                val errorView = binding.btnFindPw
                CustomSnackBar.make(errorView, R.layout.custom_snackbar_find_id_fail, R.anim.anim_show_snack_bar_from_top)
                    .setTitle("존재하지 않는 계정입니다.", null)
                    .show()
            }
        }
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