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
        anchorView: View,
        @StyleRes style: Int,
        @LayoutRes itemLayout: Int,
        @ArrayRes menuItems: Int,
    ): ListPopupWindow {
        val popupTheme = ContextThemeWrapper(anchorView.context, style)
        val listPopupWindow = ListPopupWindow(popupTheme, null, attr.listPopupWindowStyle)
        val items = anchorView.context.resources.getStringArray(menuItems)
        val adapter = ArrayAdapter(anchorView.context, itemLayout, items)
        listPopupWindow.setAdapter(adapter)
        listPopupWindow.anchorView = anchorView

        return listPopupWindow
    }

}