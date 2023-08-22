package com.example.refit.presentation.mypage

import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myScrapViewModel.setSelectedTab(MyScrapViewModel.Tab2.S_SELL)
    }

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

        binding.root.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }

        binding.rvScrapList.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                gestureDetector.onTouchEvent(e)
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
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

    private val gestureDetector by lazy {
        GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                val diffX = e2?.x?.minus(e1!!.x) ?: 0f
                if (Math.abs(diffX) > 100) { // 이 값은 원하는 스와이프 감도에 따라 조절 가능합니다.
                    onSwipe()
                }
                return super.onFling(e1, e2, velocityX, velocityY)
            }
        })
    }

    private fun onSwipe() {
        when (binding.chipGroupSell.checkedChipId) {
            R.id.chip_sell -> binding.chipGroupSell.check(R.id.chip_give)
            R.id.chip_give -> binding.chipGroupSell.check(R.id.chip_sell)
        }
    }
}