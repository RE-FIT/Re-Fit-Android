package com.example.refit.presentation.dialog.signup

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.refit.R
import com.example.refit.databinding.CustomDialogSignUpAgreeBinding
import com.example.refit.presentation.dialog.BaseDialog

class SignUpAgreeDialog: BaseDialog<CustomDialogSignUpAgreeBinding>(R.layout.custom_dialog_sign_up_agree) {

    override fun onStart() {
        super.onStart()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceHeight = Resources.getSystem().displayMetrics.heightPixels
        params?.height = (deviceHeight * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleClickCloseButton()
    }

    private fun handleClickCloseButton() {
        binding.tvDialogSignUpClose.setOnClickListener {
            dismiss()
        }
    }
}