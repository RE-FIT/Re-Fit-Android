package com.example.refit.presentation.dialog.community

import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.refit.R
import com.example.refit.databinding.CustomDialogBanOnSalesBinding
import com.example.refit.databinding.CustomDialogCommunityShippingFeeBinding
import com.example.refit.presentation.dialog.BaseDialog
import timber.log.Timber

class BanOnSalesDialog : BaseDialog<CustomDialogBanOnSalesBinding>(R.layout.custom_dialog_ban_on_sales) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleCloseButton()
        Timber.d("onViewCreated")

        binding.tvDialogAlertContentsFirst.text = getString(R.string.community_post_report_dot)
        binding.tvDialogAlertContentsSecond.text = getString(R.string.community_post_report_ban_on_sales_contents)
    }

    private fun handleCloseButton() {
        binding.btnDialogAlertOnlyTextClose.setOnClickListener {
            dismiss()
        }
    }
}