package com.example.refit.presentation.common.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.refit.R

object ClosetBindingAdapter {

    @JvmStatic
    @BindingAdapter("setTextRecommendWearing", "goalOfMonth" , "goalOfNumber")
    fun setTextRecommendWearing(view: TextView, isValid: Boolean, month: Int?, number: Int?) {
        month?.let {
            number?.let {
                if(isValid && month > 0) {
                    val wearingNumberOfMonth = number / month
                    val wearingNumberOfWeek = wearingNumberOfMonth/4
                    view.text = view.context.resources.getString(R.string.cloth_register_recommend_wearing, wearingNumberOfMonth, wearingNumberOfWeek)
                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter("setClothCategoryTextSelectionStatus")
    fun setClothCategoryTextSelectionStatus(view: TextView, status: Boolean?) {
        status?.let{
            view.isSelected = status
        }
    }
}