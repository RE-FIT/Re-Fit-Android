package com.example.refit.presentation.dialog.community

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment


abstract class CommunityBaseDialog<T: ViewDataBinding>(@LayoutRes private val layoutRes: Int): DialogFragment() {

    private var _binding: T? = null
    val binding get() = _binding ?: throw IllegalStateException("binding fail")

    override fun onStart() {
        super.onStart()
        val dialogWindow = dialog?.window
        val dialogParams = dialogWindow?.attributes

        val deviceWidth = Resources.getSystem().displayMetrics.widthPixels
        val deviceHeight = Resources.getSystem().displayMetrics.heightPixels


        dialogParams?.width = (deviceWidth * 0.8).toInt()
        dialogParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT

        dialogParams?.y = (deviceHeight * 0.15).toInt() * -1


        dialogWindow?.attributes = dialogParams

        dialogWindow?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}