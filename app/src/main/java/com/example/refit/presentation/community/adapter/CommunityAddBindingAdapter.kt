package com.example.refit.presentation.community.adapter

import android.content.res.ColorStateList
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.example.refit.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import org.w3c.dom.Text
import timber.log.Timber

@BindingAdapter("CDrawableTint")
fun setDrawableTint(view: MaterialTextView, isClicked: LiveData<Boolean>?) {
    val colorResId = if (isClicked?.value == true) R.color.green1 else R.color.dark2
    val color = ContextCompat.getColor(view.context, colorResId)
    view.compoundDrawableTintList = ColorStateList.valueOf(color)
}

@BindingAdapter("CStrokeColor")
fun setStrokeColor(view: MaterialCardView, isClicked: LiveData<Boolean>?) {
    val colorResId = if (isClicked?.value == true) R.color.green1 else R.color.dark1
    val color = ContextCompat.getColor(view.context, colorResId)
    Timber.d("strokecolor: $colorResId")
    view.strokeColor = color
}

@BindingAdapter("CTextColor")
fun setTextColor(view: TextView, isClicked: Boolean) {
    val colorResId = if (isClicked) R.color.green1 else R.color.dark2
    val color = ContextCompat.getColor(view.context, colorResId)
    view.setTextColor(color)
}

@BindingAdapter("setTextSize")
fun setText(view: TextView, size: String) {
    view.text = getSizeString(size.toInt())
}

private fun getSizeString(size: Int): String {
    return when (size) {
        0 -> "XS"
        1 -> "S"
        2 -> "M"
        3 -> "L"
        4 -> "XL"
        else -> "Unknown" // You can set a default value or handle other cases as needed
    }
}

@BindingAdapter("app:restrictText")
fun setRestrictedText(view: TextView, text: CharSequence?) {
    val maxLength = 200
    val restrictedText = text?.take(maxLength)
    view.text = restrictedText
}
