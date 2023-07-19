package com.example.refit.presentation.community

import android.os.Bundle
import android.view.View
import com.example.refit.R
import com.example.refit.databinding.FragmentPostReportBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DialogUtil.showBanOnSalesDialog
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.community.viewmodel.PostReportViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PostReportFragment : BaseFragment<FragmentPostReportBinding>(R.layout.fragment_post_report) {
    private val postReportViewModel: PostReportViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = postReportViewModel

        handleReportReasonButton()
        setOnClickQuestionMark()
        setOnClickNextButton()
    }

    private fun handleReportReasonButton() {
        binding.tvPostReportReasonFirst.setOnClickListener {
            postReportViewModel.setSelectReasonNumber(1, true)
        }
        binding.tvPostReportReasonSecond.setOnClickListener {
            postReportViewModel.setSelectReasonNumber(2, true)
        }
        binding.tvPostReportReasonThird.setOnClickListener {
            postReportViewModel.setSelectReasonNumber(3, true)
        }
        binding.tvPostReportReasonFourth.setOnClickListener {
            postReportViewModel.setSelectReasonNumber(4, true)
        }
        binding.tvPostReportReasonFifth.setOnClickListener {
            postReportViewModel.setSelectReasonNumber(5, true)
        }
    }

    private fun setOnClickQuestionMark() {
        binding.ibCommunityReportPostHelp.setOnClickListener {
            // TODO 다이얼로그 띄워주기
            showBanOnSalesDialog().show(requireActivity().supportFragmentManager, "BanOnSalesDialog")
        }
    }

    private fun setOnClickNextButton() {
        binding.btnPostReportNext.setOnClickListener {
            navigate(R.id.action_postReportFragment_to_postReportDetailFrament)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        postReportViewModel.initAlLStatus()
    }

}