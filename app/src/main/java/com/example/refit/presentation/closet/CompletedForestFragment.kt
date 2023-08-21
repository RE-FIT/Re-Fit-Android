package com.example.refit.presentation.closet

import android.os.Bundle
import android.view.View
import com.example.refit.R
import com.example.refit.databinding.FragmentCompletedForestBinding
import com.example.refit.presentation.closet.viewmodel.ForestViewModel
import com.example.refit.presentation.common.BaseFragment
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class CompletedForestFragment : BaseFragment<FragmentCompletedForestBinding>(R.layout.fragment_completed_forest) {

    private val forestViewModel: ForestViewModel by activityViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}