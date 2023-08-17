package com.example.refit.presentation.mypage

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.refit.R
import com.example.refit.databinding.FragmentScrapBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.community.viewmodel.CommunityInfoViewModel
import com.example.refit.presentation.mypage.adapter.MyScrapGiveListAdapter
import com.example.refit.presentation.mypage.adapter.MyScrapListAdapter
import com.example.refit.presentation.mypage.viewmodel.MyFeedViewModel
import com.example.refit.presentation.mypage.viewmodel.MyScrapViewModel
import com.example.refit.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ScrapFragment: BaseFragment<FragmentScrapBinding>(R.layout.fragment_scrap) {

    private val myScrapViewModel: MyScrapViewModel by sharedViewModel()
    private val infoViewModel: CommunityInfoViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var chipGroup = binding.chipGroupSell
        binding.vm = myScrapViewModel

        myScrapViewModel.selectedPostItem.observe(viewLifecycleOwner, EventObserver { postId ->
            infoViewModel.clickedGetPost(postId)
            navigate(R.id.action_myPage_myScrap_to_communityInfoFragment)
        })

        myScrapViewModel.selectedTab2.observe(viewLifecycleOwner, Observer { tab ->
            when (tab) {
                MyScrapViewModel.Tab2.S_SELL -> {
                    initScrapSellList()
                    myScrapViewModel.loadScrapSellList()
                }
                MyScrapViewModel.Tab2.S_GIVE -> {
                    initScrapGiveList()
                    myScrapViewModel.loadScrapGiveList()
                }
            }
        })

        chipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chip_sell -> {
                    myScrapViewModel.setSelectedTab(MyScrapViewModel.Tab2.S_SELL)
                }
                R.id.chip_give -> {
                    myScrapViewModel.setSelectedTab(MyScrapViewModel.Tab2.S_GIVE)
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