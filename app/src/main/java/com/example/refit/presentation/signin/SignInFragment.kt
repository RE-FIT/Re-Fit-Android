package com.example.refit.presentation.signin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.example.refit.R
import com.example.refit.databinding.FragmentSignInBinding
import com.example.refit.presentation.AccessTokenViewModel
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.CustomSnackBar
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.signin.viewmodel.SignInViewModel
import com.example.refit.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import retrofit2.Response

class SignInFragment : BaseFragment<FragmentSignInBinding>(R.layout.fragment_sign_in) {

    private val viewModel: SignInViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textWatcher()

        viewModel.error.observe(viewLifecycleOwner, EventObserver{
            val customSnackBar = CustomSnackBar.make(
                view = requireView(),
                layout = R.layout.custom_snackbar_sign_fail,
                animationId = R.anim.anim_show_snack_bar_from_top
            )

            customSnackBar.setTitle("존재하지 않는 계정입니다.", "아이디 또는 비밀번호를 다시 한 번 확인해주세요!")

            customSnackBar.show()
        })

        //로그인
        binding.signInExistingLogin.setOnClickListener {
            val id = binding.signInLoginId.text.toString()
            val password = binding.signInPassword.text.toString()
            viewModel.basicLogin(id, password)
        }


        //로그인 시 이동
        viewModel.accessToken.observe(viewLifecycleOwner, EventObserver {
            navigate(R.id.action_signInFragment_to_nav_closet)
        })

        //회원가입 이동
        binding.join.setOnClickListener(){
            navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        //아이디 비밀번호 찾기 이동
        binding.signInFindIdPassword.setOnClickListener(){
            navigate(R.id.action_signInFragment_to_findIdPasswordFragment)
        }

    }
    fun textWatcher() {
        binding.signInPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val id = binding.signInLoginId.text.toString()
                val password = binding.signInPassword.text.toString()

                //텍스트 입력시 로그인 버튼 색상 초록으로 변경
                if (id.isEmpty() || password.isEmpty()) {
                    binding.signInExistingLogin.setBackgroundResource(R.drawable.bg_solid_dark1_radius_10)
                } else {
                    binding.signInExistingLogin.setBackgroundResource(R.drawable.bg_solid_green_radius_10)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }
}