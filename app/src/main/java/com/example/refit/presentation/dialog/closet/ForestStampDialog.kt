package com.example.refit.presentation.dialog.closet

import android.os.Bundle
import android.view.View
import com.example.refit.R
import com.example.refit.databinding.CustomDialogForestAddTreeBinding
import com.example.refit.presentation.dialog.BaseDialog

class ForestStampDialog(): BaseDialog<CustomDialogForestAddTreeBinding>(R.layout.custom_dialog_forest_add_tree) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleClickConfirmButton()
    }

    private fun handleClickConfirmButton() {
        binding.cvDialogForestConfirm.setOnClickListener {
            dismiss()
        }
    }

}