package com.example.refit.presentation.dialog.mypage

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import com.example.refit.R
import com.example.refit.databinding.CustomDialogAlertOnlyTextIconLeftBinding
import com.example.refit.presentation.dialog.BaseDialog

class MyPagePwCheckDialog(
    private val iconDrawable: Drawable,
    private val title: String,
    private val content: String
) : BaseDialog<CustomDialogAlertOnlyTextIconLeftBinding>(R.layout.custom_dialog_alert_only_text_icon_left) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        build()
    }
    private fun build() {
        binding.tvDialogAlertTitle.text = this.title
        binding.tvDialogAlertContent.text = this.content
        binding.ivDialogAlertOnlyTextIcon.setImageDrawable(this.iconDrawable)
    }

}