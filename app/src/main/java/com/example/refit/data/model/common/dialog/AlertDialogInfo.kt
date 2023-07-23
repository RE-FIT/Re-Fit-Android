package com.example.refit.data.model.common.dialog

import android.graphics.drawable.Drawable

data class AlertDialogInfo(
    val iconDrawable: Drawable,
    val title: String,
    val positiveConfirm: String?,
    val negativeConfirm: String?
)
