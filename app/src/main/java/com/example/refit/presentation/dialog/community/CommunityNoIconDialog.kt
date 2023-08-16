package com.example.refit.presentation.dialog.community

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import com.example.refit.R
import com.example.refit.databinding.CustomDialogCommunityAlertNoIconBinding
import com.example.refit.presentation.dialog.AlertNoIconDialogListener

class CommunityNoIconDialog(
    private val title: String,
    private val positiveConfirm: String,
    private val negativeConfirm: String,
    private val confirmDialogInterface: AlertNoIconDialogListener
) : CommunityBaseDialog<CustomDialogCommunityAlertNoIconBinding>(R.layout.custom_dialog_community_alert_no_icon) {

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