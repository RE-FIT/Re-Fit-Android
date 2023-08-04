package com.example.refit.presentation.signin

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import com.example.refit.R
import com.example.refit.databinding.FragmentSignInBinding
import com.example.refit.presentation.AccessTokenViewModel
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.signin.viewmodel.SignInViewModel
import com.example.refit.presentation.signup.viewmodel.SignUpViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class SignInFragment : BaseFragment<FragmentSignInBinding>(R.layout.fragment_sign_in) {

    private val tokenViewModel: AccessTokenViewModel by sharedViewModel()
    private val viewModel: SignInViewModel by sharedViewModel()
    private val vm: SignUpViewModel by sharedViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textWatcher()
        //viewModel.logout()

        /*//엑세스 토큰 체크
        tokenViewModel.checkAccessToken()

        //토큰 체크 후, 만약 엑세스 토큰이 유효하다면 이동
        tokenViewModel.success.observe(viewLifecycleOwner) {
            if (it) {
                navigate(R.id.action_signInFragment_to_signUpFragment)
            }
        }*/

        /*tokenViewModel.error.observe(viewLifecycleOwner) {
            it?.let {
                Timber.d(it.toString())
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            it?.let{
                Timber.d(it.toString())
            }
        }*/

        //로그인
        //id: admin1234
        //password: AAaa1234!!
        binding.signInExistingLogin.setOnClickListener {
            val id = binding.signInLoginId.text.toString()
            val password = binding.signInPassword.text.toString()

            /*vm.signUpUser("admin1234", "AAaa1234!!","refit@gmail.com","어드민","2023/07/12", 0)*/
            /*viewModel.basicLogin("admin1234", "AAaa1234!!")*/
            //Timber.d("$id $password")
            viewModel.basicLogin(id, password)
        }


        //로그인 시 이동
        viewModel.accessToken.observe(viewLifecycleOwner) {
            navigate(R.id.action_signInFragment_to_communityFragment)
        }

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
        binding.signInPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                val id = binding.signInLoginId.text.toString()
                val password = binding.signInPassword.text.toString()

                //텍스트 입력시 버튼 색상 초록으로 변경
                if (id.isEmpty() && password.isEmpty()) {
                    binding.signInExistingLogin.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green1))
                } else {
                    binding.signInExistingLogin.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green1))
                }

            }
        })
    }


}