package com.example.refit.presentation.common

import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.refit.R
import com.example.refit.databinding.CustomSnackBarBasicBinding
import com.google.android.material.snackbar.Snackbar

class CustomSnackBar(view: View, private val message: String) {

    companion object {
        fun make(view: View, message: String) =
            CustomSnackBar(view, message)
    }

    private val context = view.context
    private val snackBar = Snackbar.make(view, "", 2000)
    private val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout

    private val inflater = LayoutInflater.from(context)
    private val snackBarBinding: CustomSnackBarBasicBinding =
        DataBindingUtil.inflate(inflater, R.layout.custom_snack_bar_basic, null, false)

    init {
        initView()
        initData()
    }

    private fun initView() {
        with(snackBarLayout) {
            val layoutParams = layoutParams as FrameLayout.LayoutParams

            val snackBarShowAnim = AnimationUtils.loadAnimation(context, R.anim.anim_show_snack_bar_from_bottom)
            val snackBarHideAnim = AnimationUtils.loadAnimation(context, R.anim.anim_hide_snack_bar)
            this.startAnimation(snackBarShowAnim)

            Handler(Looper.getMainLooper()).postDelayed({
                this.startAnimation(snackBarHideAnim)
            }, 80000L)

            layoutParams.gravity = Gravity.BOTTOM
            removeAllViews()
            setPadding(0, 0, 0, 50)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(snackBarBinding.root, 0)
        }
    }

    private fun initData() {
        snackBarBinding.tvText.text = message
    }

    fun show() {
        snackBar.show()
    }
}

