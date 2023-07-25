package com.example.refit.presentation.mypage

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.example.refit.R
import com.example.refit.databinding.FragmentMyInfoPwUpdateBinding
import com.example.refit.databinding.FragmentMyInfoUpdateBinding
import com.example.refit.presentation.common.BaseFragment

class MyInfoPwUpdateFragment : BaseFragment<FragmentMyInfoPwUpdateBinding>(R.layout.fragment_my_info_pw_update) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var message = binding.currentPw.text

        // 중복확인 버튼 비활성화
        binding.btnPwUpdate.isEnabled = false

        binding.newPw.addTextChangedListener(object : TextWatcher {

            // 입력 전
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            // 값 변경 시 실행
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.newPw.text != message) {
                    binding.btnPwUpdate.isEnabled = true
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

}