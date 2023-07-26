package com.example.refit.presentation.common

import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.annotation.AnimRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.refit.R
import com.example.refit.databinding.CustomSnackBarBasicBinding
import com.example.refit.databinding.CustomSnackbarCommunityBasicBinding
import com.google.android.material.snackbar.Snackbar

class CustomSnackBar(view: View, @LayoutRes private val  layout: Int, @AnimRes private val animationId: Int) {

    companion object {
        fun make(view: View, layout: Int, animationId: Int) =
            CustomSnackBar(view, layout, animationId)
    }

    private val context = view.context
    private val snackBar = Snackbar.make(view, "", 2000)
    private val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout

    private val inflater = LayoutInflater.from(context)
    private val snackBarBinding: ViewDataBinding =
        DataBindingUtil.inflate(inflater, layout, null, false)

    init {
        initView()
    }

    private fun initView() {
        with(snackBarLayout) {
            val layoutParams = layoutParams as FrameLayout.LayoutParams

            val snackBarShowAnim = AnimationUtils.loadAnimation(context, animationId)
            val snackBarHideAnim = AnimationUtils.loadAnimation(context, R.anim.anim_hide_snack_bar)
            this.startAnimation(snackBarShowAnim)

            Handler(Looper.getMainLooper()).postDelayed({
                this.startAnimation(snackBarHideAnim)
            }, 80000L)

            layoutParams.gravity = when(animationId) {
                R.anim.anim_show_snack_bar_from_bottom -> Gravity.BOTTOM
                else -> Gravity.TOP
            }
            removeAllViews()
            setPadding(0, 0, 0, 50)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(snackBarBinding.root, 0)
        }
    }

    fun setTitle(title: String, subtitle: String?): CustomSnackBar {
        when(layout) {
            R.layout.custom_snack_bar_basic -> {
                (snackBarBinding as CustomSnackBarBasicBinding).title = title
            }
            R.layout.custom_snackbar_community_basic -> {
                (snackBarBinding as CustomSnackbarCommunityBasicBinding).title = title
                if (subtitle != null) snackBarBinding.subtitle = subtitle
                else snackBarBinding.tvSnackbarCommunitySecond.visibility = View.GONE
            }
        }
        return this
    }

    fun show() {
        snackBar.show()
    }
}

