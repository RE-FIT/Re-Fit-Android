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
import com.example.refit.databinding.CustomDialogAlertBasicBinding

class AlertBasicDialog(
    private val iconDrawable: Drawable,
    private val title: String,
    private val positiveConfirm: String,
    private val negativeConfirm: String,
    private val confirmDialogInterface: AlertBasicDialogListener
) : DialogFragment() {

    private var _binding: CustomDialogAlertBasicBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CustomDialogAlertBasicBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        handlePositiveConfirm()
        handleNegativeConfirm()
        build()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun build() {
        binding.tvDialogAlertTitle.text = this.title
        binding.btnDialogAlertBasicPositive.text = this.positiveConfirm
        binding.btnDialogAlertBasicNegative.text = this.negativeConfirm
        binding.ivDialogAlertBasicCaution.setImageDrawable(this.iconDrawable)
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