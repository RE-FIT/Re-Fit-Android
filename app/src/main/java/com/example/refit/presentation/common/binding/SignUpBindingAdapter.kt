package com.example.refit.presentation.common.binding

import androidx.databinding.BindingAdapter
import com.example.refit.data.model.common.ResponseError
import com.google.android.material.textfield.TextInputLayout

object SignUpBindingAdapter {

    @JvmStatic
    @BindingAdapter("handleEmailCertificationError")
    fun handleEmailCertificationError(view: TextInputLayout, error: ResponseError?) {
        error?.let {
            val errorMessage = when(error.code) {
                10017 -> { "이미 존재하는 이메일입니다" }
                10018 -> { "이메일 형식이 올바르지 않습니다" }
                else -> { "이메일은 필수 정보입니다" }
            }
            view.error = errorMessage
        }
    }
}