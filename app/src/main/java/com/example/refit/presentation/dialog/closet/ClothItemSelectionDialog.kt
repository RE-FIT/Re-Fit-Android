package com.example.refit.presentation.dialog.closet

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.example.refit.R
import com.example.refit.data.model.closet.ResponseRegisteredClothes
import com.example.refit.databinding.CustomDialogClothSelectionBinding
import com.example.refit.presentation.closet.viewmodel.ClosetViewModel
import com.example.refit.presentation.dialog.BaseDialog
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class ClothItemSelectionDialog(
    private val clothInfo: ResponseRegisteredClothes,
    private val listener: ClothItemSelectionDialogListener
) :
    BaseDialog<CustomDialogClothSelectionBinding>(R.layout.custom_dialog_cloth_selection) {

    private val closetViewModel: ClosetViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.isValidGoal =
            resources.getString(R.string.registered_cloth_none_goal_remained_day)
                .toInt() != clothInfo.remainedDay
        binding.isNotCompletedGoal =
            resources.getString(R.string.registered_cloth_completed_goal_remained_day)
                .toInt() != clothInfo.remainedDay
        binding.dialog = this
        binding.vm = closetViewModel
        binding.clothInfo = clothInfo
        handleClickUpdateInfoButton()
        handleClickClothDeletion()
        handleClickCloseButton()
    }

    fun handleClickMainButton(isNotCompleteGoal: Boolean) {
        listener.onClickMainButton(isNotCompleteGoal)
        dismiss()
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