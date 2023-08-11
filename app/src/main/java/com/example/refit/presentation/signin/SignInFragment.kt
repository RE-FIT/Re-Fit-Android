package com.example.refit.presentation.signin

import android.os.Bundle
import android.view.View
import com.example.refit.R
import com.example.refit.databinding.FragmentSignInBinding
import com.example.refit.presentation.AccessTokenViewModel
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.CustomSnackBar
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.signin.viewmodel.SignInViewModel
import com.example.refit.presentation.signup.viewmodel.SignUpViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class SignInFragment : BaseFragment<FragmentSignInBinding>(R.layout.fragment_sign_in) {

    private val tokenViewModel: AccessTokenViewModel by sharedViewModel()
    private val viewModel: SignInViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*//엑세스 토큰 체크
        tokenViewModel.checkAccessToken()

        //토큰 체크 후, 만약 엑세스 토큰이 유효하다면 이동
        tokenViewModel.success.observe(viewLifecycleOwner) {
            if (it) {
                navigate(R.id.action_signInFragment_to_signUpFragment)
            }
        }*/

        tokenViewModel.error.observe(viewLifecycleOwner) {
            it?.let {
                Timber.d(it.toString())
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {

            val customSnackBar = CustomSnackBar.make(
                view = requireView(),
                layout = R.layout.custom_snackbar_sign_fail,
                animationId = R.anim.anim_show_snack_bar_from_bottom
            )

            customSnackBar.setTitle("존재하지 않는 계정입니다.", "아이디 또는 비밀번호를 다시 한 번 확인해주세요!")

            customSnackBar.show()
        }

        //로그인
        binding.signInExistingLogin.setOnClickListener {
            val id = binding.signInLoginId.text.toString()
            val password = binding.signInPassword.text.toString()
            viewModel.basicLogin(id, password)
//            viewModel.basicLogin("admin1234", "AAaa1234!!")
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
}