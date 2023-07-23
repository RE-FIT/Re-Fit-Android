package com.example.refit.presentation.signin

import android.os.Bundle
import android.view.View
import com.example.refit.R
import com.example.refit.databinding.FragmentSignInBinding
import com.example.refit.presentation.AccessTokenViewModel
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.signin.viewmodel.SignInViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SignInFragment : BaseFragment<FragmentSignInBinding>(R.layout.fragment_sign_in) {

    private val tokenViewModel: AccessTokenViewModel by sharedViewModel()
    private val viewModel: SignInViewModel by sharedViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //엑세스 토큰 체크
        tokenViewModel.checkAccessToken()

        //토큰 체크 후, 만약 엑세스 토큰이 유효하다면 이동
        tokenViewModel.success.observe(viewLifecycleOwner) {
            if (it) {
                navigate(R.id.action_signInFragment_to_signUpFragment)
            }
        }

        //로그인
        //id: admin1234
        //password: AAaa1234!!
        binding.btnMoveSignInToNextPage.setOnClickListener {
            viewModel.basicLogin("admin1234", "AAaa1234!!")
        }

        //로그인 시 이동
        viewModel.accessToken.observe(viewLifecycleOwner) {
            navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }
}