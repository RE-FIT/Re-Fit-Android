package com.example.refit.presentation.mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.refit.R
import com.example.refit.databinding.FragmentScrapBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.mypage.adapter.MyScrapGiveListAdapter
import com.example.refit.presentation.mypage.adapter.MyScrapListAdapter
import com.example.refit.presentation.mypage.viewmodel.MyFeedViewModel
import com.example.refit.presentation.mypage.viewmodel.MyScrapViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ScrapFragment: BaseFragment<FragmentScrapBinding>(R.layout.fragment_scrap) {

    private val myScrapViewModel: MyScrapViewModel by sharedViewModel()
    private val myFeedViewModel: MyFeedViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var chipGroup = binding.chipGroup

        binding.vm = myScrapViewModel

        myScrapViewModel.initStatus()

        myScrapViewModel.loadScrapSellList()
        myScrapViewModel.loadScrapGiveList()

        initSellOfScrapList()
        initGiveOfScrapList()

        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_share_scrap -> {
                    myScrapViewModel.setSelectedTab(MyFeedViewModel.Tab.SELL)
                }
                R.id.chip_share_scrap -> {
                    myScrapViewModel.setSelectedTab(MyFeedViewModel.Tab.GIVE)
                }
                else -> {
                    myScrapViewModel.setSelectedTab(MyFeedViewModel.Tab.SELL)
                }
            }
        }

        myFeedViewModel.selectedTab.observe(viewLifecycleOwner, Observer { tab ->
            when (tab) {
                MyFeedViewModel.Tab.SELL -> {
                    initSellOfScrapList()
                }
                MyFeedViewModel.Tab.GIVE -> {
                    initGiveOfScrapList()
                }
                MyFeedViewModel.Tab.BUY -> {
                    initSellOfScrapList()
                }
            }
        })
    }

    private fun initGiveOfScrapList() {
        binding.rvScrapList.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvScrapList.adapter = MyScrapGiveListAdapter(myScrapViewModel).apply {
            myScrapViewModel.myScrapGiveList.observe(viewLifecycleOwner) { list ->
                submitList(list)
            }
        }
        Log.d("initGiveOfScrapList", "로딩!")
    }

    private fun initSellOfScrapList() {
        binding.rvScrapList.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvScrapList.adapter = MyScrapListAdapter(myScrapViewModel).apply {
            myScrapViewModel.myScrapSellList.observe(viewLifecycleOwner) { list ->
                submitList(list)
            }
        }
    }
}