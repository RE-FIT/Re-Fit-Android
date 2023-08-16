package com.example.refit.presentation.findidpassword

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.navigation.Navigation
import com.example.refit.R
import com.example.refit.data.model.signup.FindIdRequest
import com.example.refit.databinding.FragmentFindIdBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.CustomSnackBar
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.findidpassword.viewModel.FindIdPasswordViewModel
import com.example.refit.presentation.findidpassword.viewModel.FindIdViewModel
import com.example.refit.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FindIdFragment : BaseFragment<FragmentFindIdBinding>(R.layout.fragment_find_id) {

    private val vm: FindIdPasswordViewModel by sharedViewModel()
    private val viewModel: FindIdViewModel by sharedViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = vm

        //아이디 찾기
        binding.btnFindIdBtn.setOnClickListener(){
            val id = binding.findIdEditName.text.toString()
            val email = binding.findIdEditEmail.text.toString()
            val findIdRequest = FindIdRequest(id, email)

            if(id.isNotEmpty() && email.isNotEmpty()){
                viewModel.findById(findIdRequest)
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
        viewModel.idSuccess.observe(viewLifecycleOwner, EventObserver{
            val action = FindIdPasswordFragmentDirections.actionFindIdPasswordFragmentToFindIdFinishFragment(viewModel.id.value)
            Navigation.findNavController(view).navigate(action)
        })

        editNickname()
        editEmail()
    }

    private fun editNickname() {
        // 이름(닉네임)
        binding.findIdEditName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                vm.setFindIdFilledStatus(0, true, s.toString())
            }
            override fun afterTextChanged(s: Editable?) { vm.setFindIdAllFilledStatus() }
        })
    }

    private fun editEmail() {
        // 이메일
        binding.findIdEditEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                vm.setFindIdFilledStatus(1, true, s.toString())
            }
            override fun afterTextChanged(s: Editable?) { vm.setFindIdAllFilledStatus() }
        })
    }


}