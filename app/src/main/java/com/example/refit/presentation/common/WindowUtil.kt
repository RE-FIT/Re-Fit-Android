package com.example.refit.presentation.common

import android.app.Activity
import androidx.core.content.ContextCompat

object WindowUtil {

    fun Activity.setStatusBarColor(color: Int) {
        window.statusBarColor = ContextCompat.getColor(this, color)
    }

    fun Activity.setNavigationBarColor(color: Int) {
        window.navigationBarColor = ContextCompat.getColor(this, color)
    }
}