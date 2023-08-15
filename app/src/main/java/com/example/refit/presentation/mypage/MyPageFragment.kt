package com.example.refit.presentation.mypage

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.refit.MainActivity
import com.example.refit.R
import com.example.refit.databinding.FragmentMyPageBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DialogUtil.createAlertBasicDialog
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.dialog.AlertBasicDialogListener
import com.example.refit.presentation.mypage.viewmodel.MyInfoViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    private val vm: MyInfoViewModel by sharedViewModel()

    private lateinit var callback : OnBackPressedCallback
    var backPressedTime : Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //날짜 셋팅
        val today = Calendar.getInstance()
        val date = SimpleDateFormat("yyyy-MM-dd 00:00:00").parse("2023-07-01 00:00:00") // 시작 날짜 임의 지정
        val dDay = (today.time.time - (date?.time ?: 0)) / (60 * 60 * 24 * 1000)
        val challengeDay = (dDay + 1).toInt()

        val activity = activity as MainActivity
/*
        // 내 정보 버튼 클릭시
        binding.myPageBtnMyInfo.setOnClickListener{
            activity.changeMyPageFragment(1)
        }

        // 내 피드 버튼 클릭시
        binding.myPageBtnMyFeed.setOnClickListener{
            activity.changeMyPageFragment(2)
        }

        // 스크랩 버튼 클릭시
        binding.myPageBtnScrap.setOnClickListener{
            activity.changeMyPageFragment(3)
        }

        // 설정 버튼 클릭시
        binding.myPageBtnSetting.setOnClickListener{
            activity.changeMyPageFragment(4)
        }*/

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

        binding.myPageName.text = vm.userNickname.value
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() - backPressedTime < 2500) {
                    activity?.finish()
                    return
                }
                Toast.makeText(activity, "한 번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
                backPressedTime = System.currentTimeMillis()
            }
        }
        activity?.onBackPressedDispatcher!!.addCallback(this, callback)
        val mainActivity = context as MainActivity
    }


    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}