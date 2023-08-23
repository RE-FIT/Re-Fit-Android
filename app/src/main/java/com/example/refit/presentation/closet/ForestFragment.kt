package com.example.refit.presentation.closet

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.refit.R
import com.example.refit.data.model.closet.ResponseForestStatusInfo
import com.example.refit.databinding.FragmentForestBinding
import com.example.refit.presentation.closet.adapter.ForestStampAdapter
import com.example.refit.presentation.closet.viewmodel.ForestViewModel
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DialogUtil
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.common.WindowUtil.setStatusBarColor
import com.example.refit.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ForestFragment : BaseFragment<FragmentForestBinding>(R.layout.fragment_forest) {

    private val forestViewModel: ForestViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.green1)
        forestViewModel.getForestInfo()
        initForestStampStatus()
        handleClickReturnButton()
        handleClickItem()
        handleGetForestInfo()
    }

    private fun handleGetForestInfo() {
        forestViewModel.forestInfo.observe(viewLifecycleOwner, EventObserver {initData ->
            binding.forestInfo = initData
            handleShowingStampDialog(initData)
            handleShowingCompletedForestWindow()
        })
    }


    private fun initForestStampStatus() {
        binding.rvForestStamp.layoutManager = GridLayoutManager(requireActivity(), 3)
        binding.rvForestStamp.adapter = ForestStampAdapter(forestViewModel).apply {
            forestViewModel.forestStamps.observe(viewLifecycleOwner, EventObserver {list ->
                submitList(list)
            })
        }
    }

    private fun handleClickReturnButton() {
        binding.btnForestReturnToCloth.setOnClickListener {
            navigate(R.id.action_forestFragment_to_nav_closet)
        }
    }

    private fun handleClickItem() {
        forestViewModel.isSelectItem.observe(viewLifecycleOwner, EventObserver { isSelectItem ->
            if(isSelectItem) {
                navigate(R.id.action_forestFragment_to_quizFragment)
            }
        })
    }

    private fun handleShowingStampDialog(initData: ResponseForestStatusInfo) {
        forestViewModel.isValidShowingDialog.observe(viewLifecycleOwner, EventObserver {isValid ->
            if(isValid) {
                DialogUtil.showForestStampDialog(initData).show(childFragmentManager, null)
                forestViewModel.stopShowingDialogEver()
            }
        })
    }

    private fun handleShowingCompletedForestWindow() {
        forestViewModel.isValidShowingCompletedWindow.observe(viewLifecycleOwner, EventObserver { isValid ->
            if(isValid) {
                navigate(R.id.action_forestFragment_to_completedForestFragment)
                forestViewModel.stopShowingCompletedForestWindow()
            }
        })
    }
}