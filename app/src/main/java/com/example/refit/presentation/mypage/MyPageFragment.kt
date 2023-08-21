package com.example.refit.presentation.mypage

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.refit.R
import com.example.refit.databinding.FragmentMyPageBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.findidpassword.FindIdPasswordFragmentDirections
import com.example.refit.presentation.mypage.viewmodel.MyInfoViewModel
import com.example.refit.presentation.mypage.viewmodel.MyViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    private val vm: MyViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        // 설정 버튼 클릭시
        binding.image.setOnClickListener(){
            val action = MyPageFragmentDirections.actionNavMyPageToImageFragment(vm.myInfoResponse.value!!.imageUrl)
            Navigation.findNavController(view).navigate(action)
        }

        vm.getMyInfo()

        vm.myInfoResponse.observe(viewLifecycleOwner, Observer {
            binding.myPageName.text = it.name
            binding.dDay.text = "D + ${it.day}"
            Glide.with(binding.root)
                .load(it.imageUrl)
                .into(binding.image)
        })
    }
}