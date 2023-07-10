package com.example.refit.presentation.common.binding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip

object CommonBindingAdapter {

    @JvmStatic
    @BindingAdapter("setCardViewGroupSelected")
    fun setCardViewGroupSelected(view: MaterialCardView, selectedCategoryId: Int?) {
        selectedCategoryId?.let {
            view.isChecked = (selectedCategoryId == view.getChildAt(0).id)
        }
    }

    @JvmStatic
    @BindingAdapter("setVisibility")
    fun setVisibility(view: View, isValid: Boolean?) {
        isValid?.let {
            when(isValid) {
                true -> view.visibility = View.VISIBLE
                else -> view.visibility = View.GONE
            }
        }
    }

    @JvmStatic
    @BindingAdapter("setImage")
    fun setImage(view: ImageView, url: String?) {
        url?.let {
            Glide.with(view)
                .load(url)
                .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("setTextGroupSelected")
    fun setTextGroupSelected(view: TextView, selectedId: Int?) {
        selectedId?.let {
            view.isSelected = (selectedId == view.id)
        }
    }
}