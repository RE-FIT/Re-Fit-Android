package com.example.refit.presentation.closet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.refit.R
import com.example.refit.databinding.FragmentForestBinding
import com.example.refit.presentation.closet.adapter.ForestStampAdapter
import com.example.refit.presentation.closet.adapter.UserRegisteredClothesAdapter
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
        forestViewModel.getForestStampStatus()
        DialogUtil.showForestStampDialog().show(childFragmentManager, null)
        initForestStampStatus()
        handleClickReturnButton()
        handleClickItem()
    }

    private fun initForestStampStatus() {
        binding.rvForestStamp.layoutManager = GridLayoutManager(requireActivity(), 3)
        binding.rvForestStamp.adapter = ForestStampAdapter(forestViewModel).apply {
            forestViewModel.forestStamp.observe(viewLifecycleOwner) { list ->
                submitList(list)
            }
        }
    }

    private fun handleClickReturnButton() {
        binding.btnForestReturnToCloth.setOnClickListener {
            navigate(R.id.action_forestFragment_to_nav_closet)
        }
    }

    private fun handleClickItem() {
        forestViewModel.selectedItem.observe(viewLifecycleOwner, EventObserver {slectedItem ->
            navigate(R.id.action_forestFragment_to_quizFragment)
        })
    }
}