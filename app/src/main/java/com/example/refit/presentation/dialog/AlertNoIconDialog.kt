package com.example.refit.presentation.dialog

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import com.example.refit.R
import com.example.refit.databinding.CustomDialogAlertNoImageBinding

class AlertNoIconDialog(
    private val title: String,
    private val positiveConfirm: String,
    private val negativeConfirm: String,
    private val confirmDialogInterface: AlertNoIconDialogListener
) : BaseDialog<CustomDialogAlertNoImageBinding>(R.layout.custom_dialog_alert_no_image) {

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