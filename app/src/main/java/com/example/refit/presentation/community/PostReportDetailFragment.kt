package com.example.refit.presentation.community

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.example.refit.R
import com.example.refit.databinding.FragmentPostReportDetailBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.CustomSnackBar
import com.example.refit.presentation.common.NavigationUtil.navigateUp
import com.example.refit.presentation.community.viewmodel.PostReportViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PostReportDetailFragment :
    BaseFragment<FragmentPostReportDetailBinding>(R.layout.fragment_post_report_detail) {
    private val vm: PostReportViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = vm
        handleHideUserPost()
        observeEditTextChanges()
        clickedBtnReportDone()
        handleDoneButton()
    }

    private fun handleHideUserPost() {
        binding.tvHideUserPost.setOnClickListener {
            vm.setHideUserPostState()
        }
    }

    private fun clickedBtnReportDone() {
        binding.btnPostReportNext.setOnClickListener {
            if(vm.isSelectReasonFifth.value == true) {
                vm.setReasonType(binding.etEtcPostReportReason.text.toString())
            }
            vm.reportUser()
            vm.plusHideUser()
            navigateUp()
            navigateUp()
            CustomSnackBar.make(requireView(), R.layout.custom_snackbar_community_basic, R.anim.anim_show_snack_bar_from_top)
                .setTitle(resources.getString(R.string.community_info_snackbar_second), resources.getString(R.string.community_info_snackbar_third)).show()
        }
    }

    private fun observeEditTextChanges() {
        binding.etEtcPostReportReason.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                vm.setCompletedInputReason(false)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isFilled = s?.isNotEmpty() == true
                vm.setCompletedInputReason(isFilled)
                vm.updateTextLength(s)
            }

            override fun afterTextChanged(s: Editable?) {
                val len = s?.length
                binding.tvPostReportTextLen.text = "글자수 제한 ($len/200)"
            }
        })
    }

    private fun handleDoneButton() {
        val status: Boolean = when (vm.isSelectReasonFifth.value) {
            true -> false
            false -> true
            else -> false
        }
        vm.setCompletedInputReason(status)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vm.initAlLStatus()
    }
}