package com.example.refit.presentation.mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.example.refit.R
import com.example.refit.databinding.FragmentMyPageBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.mypage.viewmodel.MyViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    private val vm: MyViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //날짜 셋팅
        val today = Calendar.getInstance()
        val date = SimpleDateFormat("yyyy-MM-dd 00:00:00").parse("2023-07-01 00:00:00") // 시작 날짜 임의 지정
        val dDay = (today.time.time - (date?.time ?: 0)) / (60 * 60 * 24 * 1000)
        val challengeDay = (dDay + 1).toInt()

        // 내 정보 버튼 클릭시
        binding.myPageBtnMyInfo.setOnClickListener(){
            navigate(R.id.action_myPage_to_myInfo)
        }

        // 내 피드 버튼 클릭시
        binding.myPageBtnMyFeed.setOnClickListener(){
            navigate(R.id.action_myPage_to_myFeed)
        }

        // 스크랩 버튼 클릭시
        binding.myPageBtnScrap.setOnClickListener(){
            navigate(R.id.action_myPage_to_myScrap)
        }

        // 설정 버튼 클릭시
        binding.myPageBtnSetting.setOnClickListener(){
            navigate(R.id.action_myPage_to_mySetting)
        }

        binding.dDay.text = "D + ${challengeDay}"

        vm.getMyInfo()

        vm.myInfoResponse.observe(viewLifecycleOwner, Observer {
            binding.myPageName.text = it.name
        })
    }
}