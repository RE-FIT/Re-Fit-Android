package com.example.refit.presentation.common.binding

import androidx.databinding.BindingAdapter
import com.example.refit.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

object SignUpBindingAdapter {

    @JvmStatic
    @BindingAdapter("handleErrorMessage")
    fun handleErrorMessage(view: TextInputLayout, error: String?) {
        error?.let {
            view.error = error
        }
    }

    @JvmStatic
    @BindingAdapter("handleValidMessage")
    fun handleValidMessage(view: TextInputLayout, validMessage: String?) {
        validMessage?.let {
            view.error = null
            view.boxStrokeWidth = 4
            view.helperText = validMessage
        }
    }

    @JvmStatic
    @BindingAdapter("handleInvalidMessage")
    fun handleInvalidMessage(view: TextInputLayout, invalidMessage: String?) {
        invalidMessage?.let{
            view.helperText = null
            view.boxStrokeWidth = 4
            view.error = invalidMessage
        }
    }

    @JvmStatic
    @BindingAdapter("setActivationButtonByStatus")
    fun setActivationButtonByStatus(view: MaterialButton, isValid: Boolean) {
        if (isValid) {
            view.setBackgroundColor(view.context.resources.getColor(R.color.green1, null))
            view.setTextColor(view.context.resources.getColor(R.color.white, null))
        } else {
            view.setBackgroundColor(view.context.resources.getColor(R.color.light, null))
            view.setTextColor(view.context.resources.getColor(R.color.black, null))
        }
    }

    @JvmStatic
    @BindingAdapter("setActivationSignUpButtonByStatus")
    fun setActivationSignUpButtonByStatus(view: MaterialButton, isValid: Boolean) {
        if (isValid) {
            view.setBackgroundColor(view.context.resources.getColor(R.color.green1, null))
            view.setTextColor(view.context.resources.getColor(R.color.white, null))
        } else {
            view.setBackgroundColor(view.context.resources.getColor(R.color.dark1, null))
            view.setTextColor(view.context.resources.getColor(R.color.white, null))
        }
    }

}