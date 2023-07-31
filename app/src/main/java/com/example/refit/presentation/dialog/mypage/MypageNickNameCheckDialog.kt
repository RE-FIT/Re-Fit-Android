package com.example.refit.presentation.dialog.mypage

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import com.example.refit.R
import com.example.refit.databinding.CustomDialogAlertBasicNoIconBinding
import com.example.refit.databinding.CustomDialogAlertOnlyTextBinding
import com.example.refit.presentation.dialog.AlertBasicDialogListener
import com.example.refit.presentation.dialog.BaseDialog

class MypageNickNameCheckDialog(
    private val iconDrawable: Drawable,
    private val title: String
) : BaseDialog<CustomDialogAlertOnlyTextBinding>(R.layout.custom_dialog_alert_only_text) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        build()
    }
    private fun build() {
        binding.tvDialogAlertTitle.text = this.title
        binding.ivDialogAlertOnlyTextIcon.setImageDrawable(this.iconDrawable)
    }

}