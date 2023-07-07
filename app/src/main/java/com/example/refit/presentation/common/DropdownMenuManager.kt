package com.example.refit.presentation.common

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import androidx.annotation.ArrayRes
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.ListPopupWindow
import com.google.android.material.R.attr

object DropdownMenuManager {

    fun createPopupMenu(
        context: Context,
        anchorView: View,
        @StyleRes style: Int,
        @LayoutRes itemLayout: Int,
        @ArrayRes menuItems: Int,
    ): ListPopupWindow {
        val popupTheme = ContextThemeWrapper(context, style)
        val listPopupWindow = ListPopupWindow(popupTheme, null, attr.listPopupWindowStyle)
        val items = context.resources.getStringArray(menuItems)
        val adapter = ArrayAdapter(context, itemLayout, items)
        listPopupWindow.setAdapter(adapter)
        listPopupWindow.anchorView = anchorView

        return listPopupWindow
    }

}