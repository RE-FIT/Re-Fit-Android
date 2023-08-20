package com.example.refit.presentation.dialog.community

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.refit.R
import com.example.refit.databinding.CustomDialogCommunityShippingFeeBinding
import com.example.refit.presentation.community.viewmodel.CommunityAddPostViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import timber.log.Timber

class CommunityAddShippingFeeDialog(
    private val dialogInterface: CommunityAddShippingFeeDialogListener,
    private val communityAddPostViewModel: CommunityAddPostViewModel
): BottomSheetDialogFragment() {

    private lateinit var binding: CustomDialogCommunityShippingFeeBinding
    private var shippingFeeText: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.custom_dialog_community_shipping_fee, container, false)
        binding.cmdviewmodel = communityAddPostViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        communityAddPostViewModel.setSFPriceInputCompleted("")
        handlePositiveConfirm()
        observeEditTextChanges()
    }


    private fun handlePositiveConfirm() {
        binding.btnDialogCommuntiyDone.setOnClickListener {
            shippingFeeText = binding.etDialogCommunityShippingFee.text.toString()
            val shippingFee = shippingFeeText.toInt()
            dialogInterface.onClickDone(shippingFee)
            dismiss()
        }
    }

    private fun observeEditTextChanges() {
        binding.etDialogCommunityShippingFee.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                communityAddPostViewModel.setSFPriceInputCompleted("")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isFilled = s?.isNotEmpty() == true
                communityAddPostViewModel.setFilledStatus(9, isFilled, "")
            }

            override fun afterTextChanged(s: Editable?) {
                shippingFeeText = binding.etDialogCommunityShippingFee.text.toString()
                val sfText = communityAddPostViewModel.getDecimalFormat(shippingFeeText)
                binding.tvDialogCommunityFee.text = sfText + "원"
                communityAddPostViewModel.setSFPriceInputCompleted(shippingFeeText)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}