package com.example.refit.presentation.mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.refit.R
import com.example.refit.databinding.FragmentScrapBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.mypage.adapter.MyFeedGiveListAdapter
import com.example.refit.presentation.mypage.adapter.MyFeedSellListAdapter
import com.example.refit.presentation.mypage.adapter.MyScrapGiveListAdapter
import com.example.refit.presentation.mypage.adapter.MyScrapListAdapter
import com.example.refit.presentation.mypage.viewmodel.MyFeedViewModel
import com.example.refit.presentation.mypage.viewmodel.MyScrapViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class ScrapFragment: BaseFragment<FragmentScrapBinding>(R.layout.fragment_scrap) {

    private val myScrapViewModel: MyScrapViewModel by sharedViewModel()
    private val myFeedViewModel: MyFeedViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var chipGroup = binding.chipGroupSell
        binding.vm = myScrapViewModel

        myScrapViewModel.loadScrapGiveList()
        myScrapViewModel.loadScrapSellList()

        initScrapGiveList()
        initScrapSellList()

        myScrapViewModel.selectedTab2.observe(viewLifecycleOwner, Observer { tab ->
            when (tab) {
                MyScrapViewModel.Tab2.GIVE -> {
                    initScrapGiveList()
                }
                MyScrapViewModel.Tab2.SELL -> {
                    initScrapSellList()
                }
            }
        })

        chipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chip_sell_scrap -> {
                    myScrapViewModel.setSelectedTab(MyScrapViewModel.Tab2.SELL)
                }
                R.id.chip_give_scrap -> {
                    myScrapViewModel.setSelectedTab(MyScrapViewModel.Tab2.GIVE)
                }
            }
        }
    }

    private fun initScrapGiveList() {
        binding.rvScrapList.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvScrapList.adapter = MyScrapGiveListAdapter(myScrapViewModel).apply {
            myScrapViewModel.myScrapGiveList.observe(viewLifecycleOwner) { list ->
                submitList(list)
            }
        }
    }
    private fun initScrapSellList() {
        binding.rvScrapList.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvScrapList.adapter = MyScrapListAdapter(myScrapViewModel).apply {
            myScrapViewModel.myScrapSellList.observe(viewLifecycleOwner) { list ->
                submitList(list)
            }
        }
    }
}