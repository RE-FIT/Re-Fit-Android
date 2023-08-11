package com.example.refit.presentation.mypage

import android.os.Bundle
import android.view.View
import com.example.refit.R
import com.example.refit.databinding.FragmentSettingBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DialogUtil.createAlertBasicDialog
import com.example.refit.presentation.common.DialogUtil.createAlertBasicNoIconDialog
import com.example.refit.presentation.dialog.AlertBasicDialogListener

class SettingFragment: BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {
            showMyPageLogoutDialog()
        }

        binding.btnDelete.setOnClickListener {
            showMyPageDeleteDialog()
        }
    }

    private fun showMyPageLogoutDialog() {
        createAlertBasicNoIconDialog(
            resources.getString(R.string.setting_logout_dialog_title),
            resources.getString(R.string.setting_logout_positive),
            resources.getString(R.string.setting_negative),
            object : AlertBasicDialogListener {
                override fun onClickPositive() {
                    // 로그아웃
                }

                override fun onClickNegative() {
                }

            }
        ).show(requireActivity().supportFragmentManager, null)
    }

    private fun showMyPageDeleteDialog() {
        createAlertBasicDialog(
            resources.getString(R.string.setting_account_delete_dialog_title),
            resources.getString(R.string.setting_account_delete_positive),
            resources.getString(R.string.setting_negative),
            object : AlertBasicDialogListener {
                override fun onClickPositive() {
                    // 탈퇴
                }

                override fun onClickNegative() {
                }

            }
        ).show(requireActivity().supportFragmentManager, null)
    }

}