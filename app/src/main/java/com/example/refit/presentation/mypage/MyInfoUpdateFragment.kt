package com.example.refit.presentation.mypage

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.refit.BuildConfig.BASE_URL
import com.example.refit.R
import com.example.refit.data.model.mypage.CheckNicknameResponse
import com.example.refit.data.model.mypage.NicknameCheckRequest
import com.example.refit.data.network.api.MypageApi
import com.example.refit.databinding.FragmentMyInfoUpdateBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.DialogUtil.checkNickNameDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyInfoUpdateFragment : BaseFragment<FragmentMyInfoUpdateBinding>(R.layout.fragment_my_info_update) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner = binding.spinnerGender
        var message = ""

        val updateBtn = binding.btnMyInfoUpdate
        val checkBtn = binding.btnNickNameCheck

        // 성별 선택
        spinner.adapter = ArrayAdapter.createFromResource(this.requireContext(), R.array.my_page_myInfo_gender, R.layout.mypage_spinner_gender)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    // 여자
                    0 -> {

                    }

                    // 남자
                    1 -> {

                    }
                }
            }
        }

        // 수정하기 버튼 비활성화
        updateBtn.isEnabled = false

        // 중복확인 버튼 비활성화
        checkBtn.isEnabled = false

        // 이름(닉네임) 수정
        binding.etNickname.addTextChangedListener(object : TextWatcher{
            // 입력 전
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                message = binding.etNickname.text.toString()
            }

            // 값 변경 시 실행
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 중복확인, 수정하기 버튼 활성화 --> 색깔 바뀜
                if (binding.etNickname.text.toString() != message) {
                    checkBtn.isEnabled = true
                    updateBtn.isEnabled = true
                }
            }

            // 값 변경 후
            override fun afterTextChanged(s: Editable?) {

            }
        })

        // 중복 확인 눌렀을 때
        checkBtn.setOnClickListener {
            val nickname = binding.etNickname.text.toString()

            val isNicknameAvailable = checkNicknameAvailability(nickname)

            if (isNicknameAvailable) {
                binding.ableName.isVisible = true // * 사용 가능한 이름입니다.
                checkBtn.isEnabled = false // 중복 확인 됨 --> 버튼색 dark
                updateBtn.isEnabled = false // 수정됨 --> 버튼색 dark

            } else {
                binding.enableName.isVisible = true // * 이미 사용 중인 이름입니다.
            }
        }

        // 중복확인 안 누르고 수정하기 버튼 눌렀을 때
        updateBtn.setOnClickListener {
            if (!binding.ableName.isVisible) {
                showMyPageNickNameCheckDialog()
            } else {
                updateBtn.isEnabled = false // 수정됨 --> 다시 색깔 바꾸기
            }
        }

        // 생일
        binding.etBirthday.addTextChangedListener(object : TextWatcher{
            // 입력 전
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                message = binding.etNickname.text.toString()
            }

            // 값 변경 시 실행
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 수정하기 버튼 활성화 > 색깔 바뀜
                if (binding.etNickname.text.toString() != message) {
                    checkBtn.isEnabled = true
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

    }

    private fun showMyPageNickNameCheckDialog() {
        checkNickNameDialog(
            resources.getString(R.string.my_info_nickname_check)
        ).show(requireActivity().supportFragmentManager, null)
    }

    private fun checkNicknameAvailability(nickname: String): Boolean {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(MypageApi::class.java)

        val request = NicknameCheckRequest(nickname)
        val call = apiService.checkNicknameAvailability(request)

        call.enqueue(object : Callback<CheckNicknameResponse> {
            override fun onResponse(
                call: Call<CheckNicknameResponse>,
                response: Response<CheckNicknameResponse>
            ) {
                if (response.isSuccessful) {
                    val serverResponse = response.body()

                    if (serverResponse != null && serverResponse.success) {
                        // TODO("사용 가능")
                    } else {
                        // TODO("이미 사용 중")
                    }
                } else {
                    // TODO("서버 응답 실패")
                }
            }

            override fun onFailure(call: Call<CheckNicknameResponse>, t: Throwable) {
                showToast("서버 응답 실패")
            }
        })
        return TODO("Provide the return value")
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

/*
    // 수정 버튼을 눌렀을 때
    updateBtn.setOnClickListener {
        if (!checkBtn.isPressed) { // 중복 버튼이 눌리지 않았다면
            showMyPageNickNameCheckDialog() // 다이얼로그 띄우기
        } else {
            // TODO("중복 확인 했으면 정보 수정")
        }
    }
*/
}