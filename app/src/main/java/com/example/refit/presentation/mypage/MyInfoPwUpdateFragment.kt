package com.example.refit.presentation.mypage

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.refit.R
import com.example.refit.databinding.FragmentMyInfoPwUpdateBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DialogUtil.checkPwDialog
import com.example.refit.presentation.mypage.viewmodel.MyInfoViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class MyInfoPwUpdateFragment : BaseFragment<FragmentMyInfoPwUpdateBinding>(R.layout.fragment_my_info_pw_update) {

    private val vm: MyInfoViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = vm
        binding.lifecycleOwner = this

        editCurrentPassword()
        editNewPassword()

        binding.btnPwUpdate.setOnClickListener {
            vm.updatePasswordRetrofit()

            if (vm.isUpdatedPwStatus.value == false) {
                notifyPwIncorrectDialog()
            }

            onDestroyView()
        }
    }

    fun notifyPwIncorrectDialog() {
        checkPwDialog(
            resources.getString(R.string.pw_incorrect_title),
            resources.getString(R.string.pw_incorrect_content)
        ).show(requireActivity().supportFragmentManager, null)
    }

    private fun editCurrentPassword() {
        binding.currentPw.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                vm.updateCurrentPw(s.toString())
                vm.setUpdatedPasswordStatus(0, true)
            }
            override fun afterTextChanged(s: Editable?) {
                vm.setUpdatedPasswordAllStatus()
                Timber.d("${vm.isUpdatedPwOptions.value}")
            }
        })
    }

    private fun editNewPassword() {
        binding.newPw.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                vm.updateNewPw(s.toString())
                vm.setUpdatedPasswordStatus(1, true)
            }
            override fun afterTextChanged(s: Editable?) {
                vm.setUpdatedPasswordAllStatus()
                Timber.d("${vm.isUpdatedPwOptions.value}")
            }
        })
    }
}