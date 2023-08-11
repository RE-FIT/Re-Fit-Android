package com.example.refit.presentation.common

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.refit.R
import com.example.refit.data.model.closet.ResponseForestStatusInfo
import com.example.refit.data.model.closet.ResponseRegisteredClothes
import com.example.refit.presentation.community.viewmodel.CommunityAddPostViewModel
import com.example.refit.presentation.dialog.AlertBasicDialog
import com.example.refit.presentation.dialog.AlertBasicDialogListener
import com.example.refit.presentation.dialog.AlertNoIconDialog
import com.example.refit.presentation.dialog.AlertNoIconDialogListener
import com.example.refit.presentation.dialog.closet.ClothItemSelectionDialog
import com.example.refit.presentation.dialog.closet.ClothItemSelectionDialogListener
import com.example.refit.presentation.dialog.closet.ClothRegisterPhotoDialog
import com.example.refit.presentation.dialog.closet.ClothRegisterPhotoDialogListener
import com.example.refit.presentation.dialog.closet.ForestStampDialog
import com.example.refit.presentation.dialog.community.BanOnSalesDialog
import com.example.refit.presentation.dialog.community.CommunityAddShippingFeeDialog
import com.example.refit.presentation.dialog.community.CommunityAddShippingFeeDialogListener
import com.example.refit.presentation.dialog.mypage.AlertBasicNoIconDialog
import com.example.refit.presentation.dialog.mypage.MyPagePwCheckDialog
import com.example.refit.presentation.dialog.mypage.MypageNickNameCheckDialog
import com.example.refit.presentation.dialog.mypage.ProfileRegisterPhotoDialog
import com.example.refit.presentation.dialog.mypage.ProfileRegisterPhotoDialogListener

object DialogUtil {
    // TODO(모든 종류의 다이얼로그 메세지는 여기에 정의)
    // Closet
    fun Fragment.createAlertBasicDialog(
        title: String,
        positive: String?,
        negative: String?,
        listener: AlertBasicDialogListener?
    ): AlertBasicDialog {
        val icon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_alert_circle_24)
        return AlertBasicDialog(icon!!, title, positive, negative, listener)
    }

    fun showClothRegisterPhotoDialog(listener: ClothRegisterPhotoDialogListener): ClothRegisterPhotoDialog {
        return ClothRegisterPhotoDialog(listener)
    }

    fun showClothItemSelectionDialog(
        clothInfo: ResponseRegisteredClothes,
        listener: ClothItemSelectionDialogListener
    ): ClothItemSelectionDialog {
        return ClothItemSelectionDialog(clothInfo, listener)
    }

    // Community
    fun showCommunityAddShippingFeeDiaglog(
        listener: CommunityAddShippingFeeDialogListener,
        viewModel: CommunityAddPostViewModel
    ): CommunityAddShippingFeeDialog {
        return CommunityAddShippingFeeDialog(listener, viewModel)
    }

    fun showBanOnSalesDialog(): BanOnSalesDialog { return BanOnSalesDialog() }

    fun Fragment.createAlertSirenDialog(
        title: String,
        positive: String,
        negative: String,
        listener: AlertBasicDialogListener
    ): AlertBasicDialog {
        val icon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_siren_21)
        return AlertBasicDialog(icon!!, title, positive, negative, listener)
    }
    fun Fragment.createAlertNoImageDialog(
        title: String,
        positive: String,
        negative: String,
        listener: AlertNoIconDialogListener
    ): AlertNoIconDialog {
        return AlertNoIconDialog(title, positive, negative, listener)
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

    fun showProfileRegisterPhotoDialog(listener: ProfileRegisterPhotoDialogListener): ProfileRegisterPhotoDialog {
        return ProfileRegisterPhotoDialog(listener)
    }

    fun showForestStampDialog(initData: ResponseForestStatusInfo): ForestStampDialog {
        return ForestStampDialog(initData)
    }
}