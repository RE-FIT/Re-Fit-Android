package com.example.refit.presentation.dialog.closet

import com.example.refit.data.model.closet.RegisteredClothInfoResponse

interface ClothItemSelectionDialogListener {
    fun onClickMainButton(isNotCompleteGoal: Boolean)
    fun onClickFixInfo(clothInfo: RegisteredClothInfoResponse)
    fun onClickClothDeletion(id: Int)
}