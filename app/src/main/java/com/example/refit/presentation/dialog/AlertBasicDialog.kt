package com.example.refit.presentation.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.refit.R
import com.example.refit.data.model.common.dialog.AlertDialogInfo
import com.example.refit.databinding.CustomDialogAlertBasicBinding

class AlertBasicDialog(
    private val iconDrawable: Drawable,
    private val title: String,
    private val positiveConfirm: String?,
    private val negativeConfirm: String?,
    private val confirmDialogInterface: AlertBasicDialogListener?
) : BaseDialog<CustomDialogAlertBasicBinding>(R.layout.custom_dialog_alert_basic) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dialogInfo = AlertDialogInfo(iconDrawable, title, positiveConfirm, negativeConfirm)
        handlePositiveConfirm()
        handleNegativeConfirm()
        handleCloseButton()
    }

    private fun handlePositiveConfirm() {
        binding.btnDialogAlertBasicPositive.setOnClickListener {
            confirmDialogInterface?.onClickPositive()
            dismiss()
        }
    }

    private fun handleNegativeConfirm() {
        binding.btnDialogAlertBasicNegative.setOnClickListener {
            confirmDialogInterface?.onClickNegative()
            dismiss()
        }
    }

    private fun handleCloseButton() {
        binding.tvDialogAlertBasicClose.setOnClickListener {
            dismiss()
        }
    }

}