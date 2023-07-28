package com.example.refit.presentation.mypage

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.example.refit.R
import com.example.refit.databinding.FragmentMyInfoPwUpdateBinding
import com.example.refit.databinding.FragmentMyInfoUpdateBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DialogUtil.checkNickNameDialog
import com.example.refit.presentation.common.DialogUtil.checkPwDialog
import com.example.refit.presentation.common.DialogUtil.createAlertBasicDialog
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.dialog.AlertBasicDialogListener

class MyInfoPwUpdateFragment : BaseFragment<FragmentMyInfoPwUpdateBinding>(R.layout.fragment_my_info_pw_update) {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    createAlertBasicDialog(
                        resources.getString(R.string.pw_change_delete_title),
                        resources.getString(R.string.pw_change_delete_positive),
                        resources.getString(R.string.pw_change_delete_negative),
                        object : AlertBasicDialogListener {
                            override fun onClickPositive() {
                                //clothAddViewModel.initAllStatus()
                                navigate(R.id.action_clothRegistrationFragment_to_nav_closet)
                            }

                            override fun onClickNegative() {
                            }
                        }).show(requireActivity().supportFragmentManager, null)
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var message = "000"

        // 수정 버튼 비활성화
        binding.btnPwUpdate.isEnabled = false

        if (binding.currentPw.text.equals(message)) {
            if (binding.newPw.text.length in 8..16)
                binding.btnPwUpdate.isEnabled = true
        }
        else {
            notifyPwIncorrectDialog()
        }

        binding.currentPw.addTextChangedListener(object : TextWatcher {
            // 입력 전
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            // 값 변경 시 실행
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun notifyPwIncorrectDialog() {
        checkPwDialog(
            resources.getString(R.string.pw_incorrect_title),
            resources.getString(R.string.pw_incorrect_content)
        ).show(requireActivity().supportFragmentManager, null)
    }

}