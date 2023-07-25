package com.example.refit.presentation.dialog.closet

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.refit.R
import com.example.refit.databinding.CustomDialogClothRegisterPhotoBinding
import com.example.refit.presentation.dialog.BaseDialog

class ClothRegisterPhotoDialog(
    private val confirmDialogInterface: ClothRegisterPhotoDialogListener
): BaseDialog<CustomDialogClothRegisterPhotoBinding>(R.layout.custom_dialog_cloth_register_photo) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handlePositiveConfirm()
        handleNegativeConfirm()
        handleClickClose()
    }

    private fun handlePositiveConfirm() {
        binding.btnDialogClothRegisterPhotoFromCamera.setOnClickListener {
            confirmDialogInterface.onClickTakePhoto()
            dismiss()
        }
    }

    private fun handleNegativeConfirm() {
        binding.btnDialogClothRegisterPhotoFromGallery.setOnClickListener {
            confirmDialogInterface.onClickGallery()
            dismiss()
        }
    }

    private fun handleClickClose() {
        binding.btnDialogClothRegisterPhotoCancel.setOnClickListener {
            dismiss()
        }
    }

}