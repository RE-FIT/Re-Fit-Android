package com.example.refit.presentation.mypage

import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.refit.R
import com.example.refit.databinding.FragmentMyFeedBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.community.viewmodel.CommunityInfoViewModel
import com.example.refit.presentation.community.viewmodel.CommunityViewModel
import com.example.refit.presentation.mypage.adapter.MyFeedBuyListAdapter
import com.example.refit.presentation.mypage.adapter.MyFeedGiveListAdapter
import com.example.refit.presentation.mypage.adapter.MyFeedSellListAdapter
import com.example.refit.presentation.mypage.viewmodel.MyFeedViewModel
import com.example.refit.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MyFeedFragment: BaseFragment<FragmentMyFeedBinding>(R.layout.fragment_my_feed) {

    private val myFeedViewModel: MyFeedViewModel by sharedViewModel()
    private val infoViewModel: CommunityInfoViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myFeedViewModel.setSelectedTab(MyFeedViewModel.Tab.SELL)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var chipGroup = binding.chipGroup

        binding.vm = myFeedViewModel

        myFeedViewModel.selectedPostItem.observe(viewLifecycleOwner, EventObserver { postId ->
            infoViewModel.clickedGetPost(postId)
            navigate(R.id.action_myPage_myFeed_to_communityInfoFragment)
        })

        /**
         * 라이브데이터 관리
         * */
        myFeedViewModel.successGive.observe(viewLifecycleOwner, EventObserver{
            initFeedGiveList()
        })

        myFeedViewModel.successSell.observe(viewLifecycleOwner, EventObserver{
            initFeedSellList()
        })

        myFeedViewModel.successBuy.observe(viewLifecycleOwner, EventObserver{
            initFeedBuyList()
        })

        myFeedViewModel.selectedTab.observe(viewLifecycleOwner, Observer { tab ->
            when (tab) {
                MyFeedViewModel.Tab.GIVE -> {
                    myFeedViewModel.loadFeedGiveList()
                }
                MyFeedViewModel.Tab.SELL -> {
                    myFeedViewModel.loadFeedSellList()
                }
                MyFeedViewModel.Tab.BUY -> {
                    myFeedViewModel.loadFeedBuyList()
                }
            }
        })

        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_sell -> {
                    myFeedViewModel.setSelectedTab(MyFeedViewModel.Tab.SELL)
                }
                R.id.chip_share -> {
                    myFeedViewModel.setSelectedTab(MyFeedViewModel.Tab.GIVE)
                }
                R.id.chip_buy -> {
                    myFeedViewModel.setSelectedTab(MyFeedViewModel.Tab.BUY)
                }
            }
        }

        binding.root.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    private fun initFeedGiveList() {
        binding.rvScrapList.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvScrapList.adapter = MyFeedGiveListAdapter(myFeedViewModel).apply {
            myFeedViewModel.myFeedGiveList.observe(viewLifecycleOwner) { list ->
                submitList(list)
            }
        }
    }
    private fun initFeedSellList() {
        binding.rvScrapList.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvScrapList.adapter = MyFeedSellListAdapter(myFeedViewModel).apply {
            myFeedViewModel.myFeedSellList.observe(viewLifecycleOwner) { list ->
                submitList(list)
            }
        }
    }
    private fun initFeedBuyList() {
        binding.rvScrapList.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvScrapList.adapter = MyFeedBuyListAdapter(myFeedViewModel).apply {
            myFeedViewModel.myFeedBuyList.observe(viewLifecycleOwner) { list ->
                submitList(list)
            }
        }
    }

    private val gestureDetector by lazy {
        GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                val diffX = e2?.x?.minus(e1!!.x) ?: 0f
                if (Math.abs(diffX) > 20) { // 이 값은 원하는 스와이프 감도에 따라 조절 가능합니다.
                    if (diffX > 0) {
                        onSwipeRight()
                    } else {
                        onSwipeLeft()
                    }
                }
                return super.onFling(e1, e2, velocityX, velocityY)
            }
        })
    }

    private fun onSwipeRight() {
        when (binding.chipGroup.checkedChipId) {
            R.id.chip_sell -> binding.chipGroup.check(R.id.chip_buy)
            R.id.chip_share -> binding.chipGroup.check(R.id.chip_sell)
            R.id.chip_buy -> binding.chipGroup.check(R.id.chip_share)
        }
    }

    private fun onSwipeLeft() {
        when (binding.chipGroup.checkedChipId) {
            R.id.chip_sell -> binding.chipGroup.check(R.id.chip_share)
            R.id.chip_share -> binding.chipGroup.check(R.id.chip_buy)
            R.id.chip_buy -> binding.chipGroup.check(R.id.chip_sell)
        }
    }
}