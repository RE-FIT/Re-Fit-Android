package com.example.refit.presentation.common

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.refit.R
import com.example.refit.data.model.closet.RegisteredClothInfoResponse
import com.example.refit.presentation.dialog.AlertBasicDialog
import com.example.refit.presentation.dialog.AlertBasicDialogListener
import com.example.refit.presentation.dialog.mypage.AlertBasicNoIconDialog
import com.example.refit.presentation.dialog.closet.ClothItemSelectionDialog
import com.example.refit.presentation.dialog.closet.ClothItemSelectionDialogListener
import com.example.refit.presentation.dialog.closet.ClothRegisterPhotoDialog
import com.example.refit.presentation.dialog.closet.ClothRegisterPhotoDialogListener
import com.example.refit.presentation.dialog.mypage.MyPagePwCheckDialog
import com.example.refit.presentation.dialog.mypage.MypageNickNameCheckDialog

object DialogUtil {
    // TODO(모든 종류의 다이얼로그 메세지는 여기에 정의)
    // Closet
    fun Fragment.createAlertBasicDialog(
        title: String,
        positive: String?,
        negative: String?,
        listener: AlertBasicDialogListener
    ): AlertBasicDialog {
        val icon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_alert_circle_24)
        return AlertBasicDialog(icon!!, title, positive, negative, listener)
    }

    fun showClothRegisterPhotoDialog(listener: ClothRegisterPhotoDialogListener): ClothRegisterPhotoDialog {
        return ClothRegisterPhotoDialog(listener)
    }

    fun showClothItemSelectionDialog(
        clothInfo: RegisteredClothInfoResponse,
        listener: ClothItemSelectionDialogListener
    ): ClothItemSelectionDialog {
        return ClothItemSelectionDialog(clothInfo, listener)
    }

    fun Fragment.createAlertBasicNoIconDialog(
        title: String,
        positive: String,
        negative: String,
        listener: AlertBasicDialogListener
    ): AlertBasicNoIconDialog {
        return AlertBasicNoIconDialog(title, positive, negative, listener)
    }

    fun Fragment.checkNickNameDialog(
        title: String
    ): MypageNickNameCheckDialog {
        val icon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_alert_circle_24)
        return MypageNickNameCheckDialog(icon!!, title)
    }

    fun Fragment.checkPwDialog(
        title: String,
        content: String
    ): MyPagePwCheckDialog {
        val icon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_alert_circle_24)
        return MyPagePwCheckDialog(icon!!, title, content)
    }
}