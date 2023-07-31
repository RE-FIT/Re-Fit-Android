package com.example.refit.presentation.dialog.mypage

import android.os.Bundle
import android.view.View
import com.example.refit.R
import com.example.refit.databinding.CustomDialogClothRegisterPhotoBinding
import com.example.refit.databinding.CustomDialogProfileRegisterPhotoBinding
import com.example.refit.presentation.dialog.BaseDialog

class ProfileRegisterPhotoDialog(
    private val confirmDialogInterface: ProfileRegisterPhotoDialogListener
): BaseDialog<CustomDialogProfileRegisterPhotoBinding>(R.layout.custom_dialog_profile_register_photo) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handlePositiveConfirm()
        handleNegativeConfirm()
        handleClickClose()
    }

    private fun handlePositiveConfirm() {
        binding.btnDialogClothRegisterPhotoFromCamera.setOnClickListener {
            confirmDialogInterface.onClickGallery()
            dismiss()
        }
    }

    private fun handleNegativeConfirm() {
        binding.btnDialogClothRegisterPhotoFromGallery.setOnClickListener {
            dismiss()
        }
    }

    private fun handleClickClose() {
        binding.btnDialogClothRegisterPhotoCancel.setOnClickListener {
            dismiss()
        }
    }

}