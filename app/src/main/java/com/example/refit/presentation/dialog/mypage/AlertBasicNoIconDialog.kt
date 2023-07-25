package com.example.refit.presentation.dialog.mypage

import android.os.Bundle
import android.view.View
import com.example.refit.R
import com.example.refit.databinding.CustomDialogAlertBasicNoIconBinding
import com.example.refit.presentation.dialog.AlertBasicDialogListener
import com.example.refit.presentation.dialog.BaseDialog

class AlertBasicNoIconDialog(
    private val title: String,
    private val positiveConfirm: String,
    private val negativeConfirm: String,
    private val confirmDialogInterface: AlertBasicDialogListener
) : BaseDialog<CustomDialogAlertBasicNoIconBinding>(R.layout.custom_dialog_alert_basic_no_icon) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handlePositiveConfirm()
        handleNegativeConfirm()
        build()
    }
    private fun build() {
        binding.tvDialogAlertTitle.text = this.title
        binding.btnDialogAlertBasicPositive.text = this.positiveConfirm
        binding.btnDialogAlertBasicNegative.text = this.negativeConfirm
    }

    private fun handlePositiveConfirm() {
        binding.btnDialogAlertBasicPositive.setOnClickListener {
            confirmDialogInterface.onClickPositive()
            dismiss()
        }
    }

    private fun handleNegativeConfirm() {
        binding.btnDialogAlertBasicNegative.setOnClickListener {
            confirmDialogInterface.onClickNegative()
            dismiss()
        }
    }

}