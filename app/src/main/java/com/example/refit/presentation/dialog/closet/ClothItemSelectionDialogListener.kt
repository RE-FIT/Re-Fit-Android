package com.example.refit.presentation.dialog.closet

import com.example.refit.data.model.closet.ResponseRegisteredClothes

interface ClothItemSelectionDialogListener {
    fun onClickMainButton(isNotCompleteGoal: Boolean)
    fun onClickFixInfo(clothInfo: ResponseRegisteredClothes)
    fun onClickClothDeletion(id: Int)
}