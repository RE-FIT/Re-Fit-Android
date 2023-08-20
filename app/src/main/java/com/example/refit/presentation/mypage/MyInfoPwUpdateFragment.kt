package com.example.refit.presentation.mypage

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.example.refit.R
import com.example.refit.data.model.mypage.PasswordUpdateRequest
import com.example.refit.databinding.FragmentMyInfoPwUpdateBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DialogUtil.checkPwFailDialog
import com.example.refit.presentation.common.DialogUtil.checkPwSuccessDialog
import com.example.refit.presentation.common.DialogUtil.createAlertBasicDialog
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.common.NavigationUtil.navigateUp
import com.example.refit.presentation.dialog.AlertBasicDialogListener
import com.example.refit.presentation.mypage.viewmodel.MyInfoViewModel
import com.example.refit.presentation.mypage.viewmodel.PwChangeViewModel
import com.example.refit.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MyInfoPwUpdateFragment : BaseFragment<FragmentMyInfoPwUpdateBinding>(R.layout.fragment_my_info_pw_update) {

    private val vm: PwChangeViewModel by sharedViewModel()
    private val myInfoViewModel: MyInfoViewModel by sharedViewModel()
    private var flag = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = vm
        binding.lifecycleOwner = this

        if (myInfoViewModel.type.value != null) {
            notifyBlockChangeDialog()
            binding.currentPw.isEnabled = false
            binding.newPw.isEnabled = false
        }

        //패스워드 변경 API 호출
        binding.btnPwUpdate.setOnClickListener {
            val pw = binding.currentPw.text.toString()
            val next = binding.newPw.text.toString()

            if(pw.isNotEmpty() && next.isNotEmpty()){
                vm.updatePassword(PasswordUpdateRequest(pw,next))
            }
        }

        //성공 처리
        vm.changeSuccess.observe(viewLifecycleOwner, EventObserver{
            notifyPwCorrectDialog()
            vm.init()
        })

        //에러 처리
        vm.error.observe(viewLifecycleOwner, EventObserver{
            notifyPwIncorrectDialog()
            showMyInfoBackPressedDialog()
        })

        vm.pw.observe(viewLifecycleOwner) {
            if (flag == 0) {
                showMyInfoBackPressedDialog()
            }
        }

        vm.nextPw.observe(viewLifecycleOwner) {
            if (flag == 0) {
                showMyInfoBackPressedDialog()
            }
        }
    }

    private fun notifyPwIncorrectDialog() {
        checkPwFailDialog(
            resources.getString(R.string.pw_incorrect_title),
            resources.getString(R.string.pw_incorrect_content)
        ).show(requireActivity().supportFragmentManager, null)

        flag = 0
    }

    private fun notifyPwCorrectDialog() {
        checkPwSuccessDialog(
            resources.getString(R.string.pw_correct_content)
        ).show(requireActivity().supportFragmentManager, null)

        flag = 1
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vm.init()
    }

    private fun showMyInfoBackPressedDialog() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    createAlertBasicDialog(
                        resources.getString(R.string.pw_change_delete_title),
                        resources.getString(R.string.pw_change_delete_positive),
                        resources.getString(R.string.pw_change_delete_negative),
                        object : AlertBasicDialogListener {
                            override fun onClickPositive() {
                                navigateUp()
                            }

                            override fun onClickNegative() {
                            }
                        }).show(requireActivity().supportFragmentManager, null)
                }
            })
    }

    private fun notifyBlockChangeDialog() {
        checkPwFailDialog(
            resources.getString(R.string.block_change_to_pw_title),
            resources.getString(R.string.block_change_to_pw_content)
        ).show(requireActivity().supportFragmentManager, null)
    }
}