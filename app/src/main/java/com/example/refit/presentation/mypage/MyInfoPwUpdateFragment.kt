package com.example.refit.presentation.mypage

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.example.refit.R
import com.example.refit.databinding.FragmentMyInfoPwUpdateBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DialogUtil.checkPwDialog
import com.example.refit.presentation.mypage.viewmodel.MyInfoViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MyInfoPwUpdateFragment : BaseFragment<FragmentMyInfoPwUpdateBinding>(R.layout.fragment_my_info_pw_update) {

    private val vm: MyInfoViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = vm
        binding.lifecycleOwner = this

        editNewPassword()
        pressUpdatePwBtn()
    }

    private fun notifyPwIncorrectDialog() {
        checkPwDialog(
            resources.getString(R.string.pw_incorrect_title),
            resources.getString(R.string.pw_incorrect_content)
        ).show(requireActivity().supportFragmentManager, null)
    }

    private fun editNewPassword() {
        binding.newPw.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                vm.updateCurrentPw(s.toString())
            }
            override fun afterTextChanged(s: Editable?) { }
        })
    }

    private fun pressUpdatePwBtn() {
        vm.isCheckUpdatedPw.observe(viewLifecycleOwner) {

        }
    }

}