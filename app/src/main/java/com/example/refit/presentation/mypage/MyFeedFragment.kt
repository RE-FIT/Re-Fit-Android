package com.example.refit.presentation.mypage

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.refit.R
import com.example.refit.databinding.FragmentMyFeedBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.mypage.adapter.MyFeedListAdapter
import com.example.refit.presentation.mypage.viewmodel.MyFeedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MyFeedFragment : BaseFragment<FragmentMyFeedBinding>(R.layout.fragment_my_feed) {

    private val myFeedViewModel: MyFeedViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = myFeedViewModel

        myFeedViewModel.loadCommunityList()
        initScrapList()
    }

    private fun initScrapList() {
        binding.rvScrapList.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvScrapList.adapter = MyFeedListAdapter(myFeedViewModel).apply {
            myFeedViewModel.communityList.observe(viewLifecycleOwner) { list ->
                submitList(list)
            }
        }
    }

}