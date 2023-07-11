package com.example.refit.presentation.common.binding

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.refit.R
import com.example.refit.presentation.closet.viewmodel.ClosetViewModel
import com.google.android.material.card.MaterialCardView

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

    @JvmStatic
    @BindingAdapter("clearEditTextFocusByStatus")
    fun clearEditTextFocusByStatus(view: EditText, status: Boolean?) {
        status?.let {
            if(status) {
                view.clearFocus()
            }
        }
    }

    @JvmStatic
    @BindingAdapter("setPartOfTextColor", "startIndex", "endIndex")
    fun setPartOfTextColor(view: TextView, color: Int?, startIndex: Int, endIndex: Int) {
        color?.let {
            val spannableString = SpannableString(view.text.toString())
            val highlightColor = ForegroundColorSpan(color)
            spannableString.setSpan(highlightColor, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            view.text = spannableString
        }
    }

}