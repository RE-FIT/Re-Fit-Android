package com.example.refit.presentation.common.binding

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.refit.R
import com.example.refit.presentation.dialog.AlertBasicDialog
import com.example.refit.presentation.dialog.AlertBasicDialogListener

object DialogUtil {
    // TODO(모든 종류의 다이얼로그 메세지는 여기에 정의)

    // Closet
    fun Fragment.showDeleteClothConfirmDialog(listener: AlertBasicDialogListener) {
        val icon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_alert_circle_24)
        val dialog = AlertBasicDialog(
            icon!!,
            resources.getString(R.string.closet_dialog_cloth_delete_title),
            resources.getString(R.string.closet_dialog_cloth_delete_positive),
            resources.getString(R.string.closet_dialog_cloth_delete_negative),
            listener
        )
        dialog.show(requireActivity().supportFragmentManager, "AlertDialog")
    }
}