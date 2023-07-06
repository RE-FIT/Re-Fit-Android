package com.example.refit.presentation.common.binding

import androidx.databinding.BindingAdapter
import com.google.android.material.card.MaterialCardView

object LayoutBinding {

    @JvmStatic
    @BindingAdapter("setCardViewGroupSelected")
    fun setCardViewGroupSelected(view: MaterialCardView, selectedCategoryId: Int?) {
        selectedCategoryId?.let {
            view.isChecked = (selectedCategoryId == view.getChildAt(0).id)
        }
    }
}