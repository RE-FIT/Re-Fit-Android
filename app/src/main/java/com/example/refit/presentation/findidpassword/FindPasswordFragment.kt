package com.example.refit.presentation.findidpassword

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.navigation.Navigation
import com.example.refit.R
import com.example.refit.data.model.signup.FindPasswordRequest
import com.example.refit.databinding.FragmentFindPasswordBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.CustomSnackBar
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.findidpassword.viewModel.FindIdPasswordViewModel
import com.example.refit.presentation.findidpassword.viewModel.FindPwViewModel
import com.example.refit.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FindPasswordFragment : BaseFragment<FragmentFindPasswordBinding>(R.layout.fragment_find_password) {

    private val viewModel: FindPwViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        binding.lifecycleOwner = this

        //비밀번호 찾기
        binding.btnFindPw.setOnClickListener(){
            val name = binding.findIdEditName.text.toString()
            val id = binding.findPwEditId.text.toString()
            val email = binding.findIdEditEmail.text.toString()
            val findPasswordRequest = FindPasswordRequest(name, email, id)

            if(name.isNotEmpty() && id.isNotEmpty() && email.isNotEmpty()){
                viewModel.findByPassword(findPasswordRequest)
            }
        }

        viewModel.error.observe(viewLifecycleOwner, EventObserver{
            val customSnackBar = CustomSnackBar.make(
                view = requireView(),
                layout = R.layout.custom_snackbar_find_id_fail,
                animationId = R.anim.anim_show_snack_bar_from_top
            )

            customSnackBar.setTitle("존재하지 않는 계정입니다.", null)
            customSnackBar.show()
        })

        //아이디 찾기 성공 시 이동
        viewModel.pwSuccess.observe(viewLifecycleOwner, EventObserver{
            val action = FindIdPasswordFragmentDirections.actionFindIdPasswordFragmentToFindPasswordFinishFragment(binding.findIdEditEmail.text.toString())
            Navigation.findNavController(view).navigate(action)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.init()
    }
}