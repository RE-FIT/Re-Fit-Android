package com.example.refit.presentation.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.refit.MainActivity
import com.example.refit.R
import com.example.refit.databinding.FragmentSettingBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DialogUtil.createAlertBasicDialog
import com.example.refit.presentation.common.DialogUtil.createAlertBasicNoIconDialog
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.dialog.AlertBasicDialog
import com.example.refit.presentation.dialog.AlertBasicDialogListener
import com.example.refit.presentation.signin.viewmodel.SignInViewModel
import com.example.refit.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SettingFragment: BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    private val viewModel: SignInViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnString.setOnClickListener {
            navigate(R.id.action_myPage_mySetting_to_termsFragment)
        }

        binding.btnString2.setOnClickListener {
            navigate(R.id.action_myPage_mySetting_to_policyFragment)
        }

        binding.btnLogout.setOnClickListener {
            showMyPageLogoutDialog()
        }

        binding.btnDelete.setOnClickListener {
            showMyPageDeleteDialog()
        }

        viewModel.logoutSuccess.observe(viewLifecycleOwner, EventObserver {
            requireActivity().finish()
            val restartIntent = Intent(context, MainActivity::class.java)
            startActivity(restartIntent)
        })
    }

    private fun showMyPageLogoutDialog() {
        createAlertBasicNoIconDialog(
            resources.getString(R.string.setting_logout_dialog_title),
            resources.getString(R.string.setting_logout_positive),
            resources.getString(R.string.setting_negative),
            object : AlertBasicDialogListener {
                override fun onClickPositive() {
                    viewModel.logout()
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