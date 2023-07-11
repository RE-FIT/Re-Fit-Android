package com.example.refit.presentation.common

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.refit.R
import com.example.refit.presentation.dialog.AlertBasicDialog
import com.example.refit.presentation.dialog.AlertBasicDialogListener
import com.example.refit.presentation.dialog.closet.ClothRegisterPhotoDialog
import com.example.refit.presentation.dialog.closet.ClothRegisterPhotoDialogListener

object DialogUtil {
    // TODO(모든 종류의 다이얼로그 메세지는 여기에 정의)
    // Closet
    fun Fragment.createAlertBasicDialog(
        title: String,
        positive: String,
        negative: String,
        listener: AlertBasicDialogListener
    ): AlertBasicDialog {
        val icon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_alert_circle_24)
        return AlertBasicDialog(icon!!, title, positive, negative, listener)
    }

    fun Fragment.showsClothRegisterPhotoDialog(listener: ClothRegisterPhotoDialogListener) {
        val dialog = ClothRegisterPhotoDialog(listener)
        dialog.show(requireActivity().supportFragmentManager, "ClothRegisterPhotoDialog")
    }
}