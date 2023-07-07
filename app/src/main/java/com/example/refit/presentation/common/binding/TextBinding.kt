package com.example.refit.presentation.common.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter

object TextBinding {

    @JvmStatic
    @BindingAdapter("setTextGroupSelected")
    fun setTextGroupSelected(view: TextView, selectedId: Int?) {
        selectedId?.let {
            view.isSelected = (selectedId == view.id)
        }
    }
}