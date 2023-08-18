package com.example.refit.presentation.dialog.mypage

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import com.example.refit.R
import com.example.refit.databinding.CustomDialogAlertOnlyTextIconLeftVer2Binding
import com.example.refit.presentation.dialog.BaseDialog

class MyPagePwCheckSuccessDialog(
    private val iconDrawable: Drawable,
    private val title: String
) : BaseDialog<CustomDialogAlertOnlyTextIconLeftVer2Binding>(R.layout.custom_dialog_alert_only_text_icon_left_ver2) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        build()
    }
    private fun build() {
        binding.tvDialogAlertTitle.text = this.title
        binding.ivDialogAlertOnlyTextIcon.setImageDrawable(this.iconDrawable)
    }

}