package com.example.refit.presentation.dialog.closet

import android.os.Bundle
import android.view.View
import com.example.refit.R
import com.example.refit.data.model.closet.RegisteredClothInfoResponse
import com.example.refit.databinding.CustomDialogClothSelectionBinding
import com.example.refit.presentation.dialog.BaseDialog

class ClothItemSelectionDialog(
    private val clothInfo: RegisteredClothInfoResponse,
    private val listener: ClothItemSelectionDialogListener
) :
    BaseDialog<CustomDialogClothSelectionBinding>(R.layout.custom_dialog_cloth_selection) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.isNotCompleteGoal = clothInfo.progress < 100
        handleClickMainButton()
        handleClickUpdateInfoButton()
        handleClickClothDeletion()
        handleClickCloseButton()
    }

    private fun handleClickMainButton() {
        binding.btnDialogClothSelectionOptionMain.setOnClickListener {
            listener.onClickMainButton(clothInfo.progress < 100)
            dismiss()
        }
    }

    private fun handleClickUpdateInfoButton() {
        binding.btnDialogClothSelectionOptionFix.setOnClickListener {
            listener.onClickFixInfo(clothInfo)
            dismiss()
        }
    }

    private fun handleClickClothDeletion() {
        binding.btnDialogClothSelectionOptionDelete.setOnClickListener {
            listener.onClickClothDeletion(clothInfo.id)
            dismiss()
        }
    }

    private fun handleClickCloseButton() {
        binding.btnDialogClothSelectionClose.setOnClickListener {
            dismiss()
        }
    }


}