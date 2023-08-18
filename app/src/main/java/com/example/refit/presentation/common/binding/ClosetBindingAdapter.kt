package com.example.refit.presentation.common.binding

import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.refit.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import kotlin.math.abs

object ClosetBindingAdapter {

    @JvmStatic
    @BindingAdapter("setTextRecommendWearing", "goalOfMonth", "goalOfNumber")
    fun setTextRecommendWearing(view: TextView, isValid: Boolean, month: Int?, number: Int?) {
        month?.let {
            number?.let {
                if (isValid && month > 0) {
                    val wearingNumberOfMonth = number / month
                    val wearingNumberOfWeek = wearingNumberOfMonth / 4
                    view.text = view.context.resources.getString(
                        R.string.cloth_register_recommend_wearing,
                        wearingNumberOfMonth,
                        wearingNumberOfWeek
                    )
                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter("setClothCategoryTextSelectionStatus")
    fun setClothCategoryTextSelectionStatus(view: TextView, status: Boolean?) {
        status?.let {
            view.isSelected = status
        }
    }

    @JvmStatic
    @BindingAdapter("setGoalDay")
    fun setGoalDay(view: TextView, remainedDay: Int) {
        when (remainedDay) {
            -7777 -> {
                view.text = view.context.resources.getString(
                    R.string.registered_cloth_info_d_day_none_goal
                )
            }

            7777 -> {
                view.text = view.context.resources.getString(
                    R.string.registered_cloth_info_d_day_complete
                )
            }

            else -> {
                view.text = view.context.resources.getString(
                    R.string.registered_cloth_info_d_day,
                    remainedDay.toString()[0],
                    abs(remainedDay)
                )
            }
        }
    }

    @JvmStatic
    @BindingAdapter("setButtonBackgroundTintByStatus")
    fun setButtonBackgroundTintByStatus(view: MaterialButton, status: Boolean) {
        when(status) {
            true -> view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.green1))
            else -> view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.dark1))
        }
    }

    @JvmStatic
    @BindingAdapter("setForestStampImage")
    fun setForestStampImage(view: ImageView, treeId: Int) {
        val treeDrawable = when(treeId) {
            0 -> R.drawable.ic_tree_first_65
            1 -> R.drawable.ic_tree_second_55
            2 -> R.drawable.ic_tree_third_59
            else -> R.drawable.ic_tree_first_65
        }
        CommonBindingAdapter.setImage(view, treeDrawable)
    }

    @JvmStatic
    @BindingAdapter("setQuizCategory")
    fun setQuizCategory(view: TextView, categoryId: Int?) {
        categoryId?.let{
            val categoryList = view.context.resources.getStringArray(R.array.closet_forest_quiz_category).toList()
            if(categoryId > 0) {
                view.text = categoryList[it-1]
            }
        }
    }

    @JvmStatic
    @BindingAdapter("setQuizAnswer", "isRequestAnswer")
    fun setQuizAnswer(view: TextView, isAnswer: Boolean, isRequestAnswer: Boolean) {
        if(isRequestAnswer) {
            if(isAnswer) view.text = view.context.resources.getString(R.string.closet_quiz_question_positive)
            else view.text = view.context.resources.getString(R.string.closet_quiz_question_negative)
        } else {
            view.text = view.context.resources.getString(R.string.closet_quiz_question)
        }
    }

    @JvmStatic
    @BindingAdapter("setClosetCategoryShadow")
    fun setClosetCategoryShadow(view: CardView, isChecked: Boolean) {
        val shadowColor = when(isChecked) {
            true -> R.color.green1
            false -> R.color.transparent
        }
        view.outlineSpotShadowColor = view.context.resources.getColor(shadowColor, null)
        view.outlineAmbientShadowColor = view.context.resources.getColor(shadowColor, null)
    }

}