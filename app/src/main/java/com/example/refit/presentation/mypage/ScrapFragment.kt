package com.example.refit.presentation.mypage

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.refit.R
import com.example.refit.databinding.FragmentScrapBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.community.adapter.CommunityListAdapter
import com.example.refit.presentation.mypage.adapter.MyScrapListAdapter
import com.example.refit.presentation.mypage.viewmodel.MyScrapViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ScrapFragment: BaseFragment<FragmentScrapBinding>(R.layout.fragment_scrap) {

    private val myScrapViewModel: MyScrapViewModel by sharedViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = myScrapViewModel

        myScrapViewModel.loadCommunityList()
        initScrapList()
    }

    private fun initScrapList() {
        binding.rvScrapList.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvScrapList.adapter = MyScrapListAdapter(myScrapViewModel).apply {
            myScrapViewModel.communityList.observe(viewLifecycleOwner) { list ->
                submitList(list)
            }
        }
    }
}