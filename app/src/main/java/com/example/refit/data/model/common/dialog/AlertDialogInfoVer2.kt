package com.example.refit.data.model.common.dialog

import android.graphics.drawable.Drawable

data class AlertDialogInfoVer2(
    val iconDrawable: Drawable,
    val title: String,
    val subTitle: String,
    val positiveConfirm: String?,
    val negativeConfirm: String?
)
