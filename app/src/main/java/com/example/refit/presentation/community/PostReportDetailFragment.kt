package com.example.refit.presentation.community

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.refit.R
import com.example.refit.databinding.FragmentPostReportDetailBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.CustomSnackBar
import com.example.refit.presentation.common.NavigationUtil.navigateUp
import com.example.refit.presentation.community.viewmodel.PostReportViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class PostReportDetailFrament :
    BaseFragment<FragmentPostReportDetailBinding>(R.layout.fragment_post_report_detail) {
    private val postReportViewModel: PostReportViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = postReportViewModel
        handleHideUserPost()
        observeEditTextChanges()
        clickedBtnReportDone()
        handleDoneButton()
    }

    private fun handleHideUserPost() {
        binding.tvHideUserPost.setOnClickListener {
            postReportViewModel.setHideUserPostState()
        }
    }

    private fun clickedBtnReportDone() {
        binding.btnPostReportNext.setOnClickListener {
            val etcReason = binding.etEtcPostReportReason.text
            navigateUp()
            navigateUp()
            CustomSnackBar.make(requireView(), R.layout.custom_snackbar_community_basic, R.anim.anim_show_snack_bar_from_top)
                .setTitle(resources.getString(R.string.community_info_snackbar_second), resources.getString(R.string.community_info_snackbar_third)).show()
        }
    }

    private fun observeEditTextChanges() {
        binding.etEtcPostReportReason.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                postReportViewModel.setCompletedInputReason(false)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isFilled = s?.isNotEmpty() == true
                postReportViewModel.setCompletedInputReason(isFilled)
                postReportViewModel.updateTextLength(s)
            }

            override fun afterTextChanged(s: Editable?) {
                val len = s?.length
                binding.tvPostReportTextLen.text = "글자수 제한 ($len/200)"
            }
        })
    }

    private fun handleDoneButton() {
        val status: Boolean = when (postReportViewModel.isSelectReasonFifth.value) {
            true -> false
            false -> true
            else -> false
        }
        postReportViewModel.setCompletedInputReason(status)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        postReportViewModel.initAlLStatus()
    }
}