package com.example.refit.presentation.mypage

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.example.refit.R
import com.example.refit.databinding.FragmentMyInfoPwUpdateBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DialogUtil.checkPwDialog
import com.example.refit.presentation.common.DialogUtil.createAlertBasicDialog
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.dialog.AlertBasicDialogListener
import com.example.refit.presentation.mypage.viewmodel.MyInfoViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MyInfoPwUpdateFragment : BaseFragment<FragmentMyInfoPwUpdateBinding>(R.layout.fragment_my_info_pw_update) {

    private val vm: MyInfoViewModel by sharedViewModel()

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
                                //vm.initAllStatus()
                                navigate(R.id.action_myInfo_to_nav_my_page)
                            }

                            override fun onClickNegative() {
                            }
                        }).show(requireActivity().supportFragmentManager, null)
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun notifyPwIncorrectDialog() {
        checkPwDialog(
            resources.getString(R.string.pw_incorrect_title),
            resources.getString(R.string.pw_incorrect_content)
        ).show(requireActivity().supportFragmentManager, null)
    }

}