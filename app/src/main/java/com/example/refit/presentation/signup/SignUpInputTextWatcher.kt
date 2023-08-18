package com.example.refit.presentation.signup

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputLayout

abstract class SignUpInputTextWatcher(
    private val containerView: TextInputLayout
) : TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun afterTextChanged(text: Editable?) {
        text?.let { inputText ->
            if (inputText.isEmpty()) {
                containerView.error = null
                containerView.boxStrokeWidth = 0
                containerView.helperText = null
            }
        }
    }
}