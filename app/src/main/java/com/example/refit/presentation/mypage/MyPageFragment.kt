package com.example.refit.presentation.mypage

import android.os.Bundle
import android.view.View
import androidx.core.view.forEach
import androidx.fragment.app.FragmentManager
import com.example.refit.MainActivity
import com.example.refit.R
import com.example.refit.databinding.FragmentMyPageBinding
import com.example.refit.presentation.common.BaseFragment

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as MainActivity

        // 내 정보 버튼 클릭시
        binding.myPageBtnMyInfo.setOnClickListener{
            activity.changeFragment(1)
        }

        // 내 피드 버튼 클릭시
        binding.myPageBtnMyFeed.setOnClickListener{
            activity.changeFragment(2)
        }

        // 스크랩 버튼 클릭시
        binding.myPageBtnScrap.setOnClickListener{
            activity.changeFragment(3)
        }

        // 설정 버튼 클릭시
        binding.myPageBtnSetting.setOnClickListener{
            activity.changeFragment(4)
        }
    }
}