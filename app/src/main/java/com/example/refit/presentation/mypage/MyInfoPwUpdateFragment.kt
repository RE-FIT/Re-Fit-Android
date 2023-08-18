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
import com.example.refit.presentation.dialog.AlertBasicDialogListener
import com.example.refit.presentation.mypage.viewmodel.PwChangeViewModel
import com.example.refit.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MyInfoPwUpdateFragment : BaseFragment<FragmentMyInfoPwUpdateBinding>(R.layout.fragment_my_info_pw_update) {

    private val vm: PwChangeViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = vm
        binding.lifecycleOwner = this

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
//            requireActivity().finish()
//            val restartIntent = Intent(context, MainActivity::class.java)
//            startActivity(restartIntent)
            vm.isChange(false)
            notifyPwCorrectDialog()
        })

        //에러 처리
        vm.error.observe(viewLifecycleOwner, EventObserver{
            notifyPwIncorrectDialog()
        })

        // 뒤로 가기 처리
        vm.pw.observe(viewLifecycleOwner) {
            vm.isChange(true)
        }

        vm.nextPw.observe(viewLifecycleOwner) {
            vm.isChange(true)
        }

        showMyInfoBackPressedDialog()

    }

    private fun notifyPwIncorrectDialog() {
        checkPwFailDialog(
            resources.getString(R.string.pw_incorrect_title),
            resources.getString(R.string.pw_incorrect_content)
        ).show(requireActivity().supportFragmentManager, null)
    }

    private fun notifyPwCorrectDialog() {
        checkPwSuccessDialog(
            resources.getString(R.string.pw_correct_content)
        ).show(requireActivity().supportFragmentManager, null)

        vm.init()
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
                    if (vm.isChange.value == true) {
                        createAlertBasicDialog(
                            resources.getString(R.string.pw_change_delete_title),
                            resources.getString(R.string.pw_change_delete_positive),
                            resources.getString(R.string.pw_change_delete_negative),
                            object : AlertBasicDialogListener {
                                override fun onClickPositive() {
                                    navigate(R.id.action_myInfo_to_nav_my_page)
                                }

                                override fun onClickNegative() {
                                }
                            }).show(requireActivity().supportFragmentManager, null)
                    } else {
                        navigate(R.id.action_myInfo_to_nav_my_page)
                    }
                }
            })
    }
}