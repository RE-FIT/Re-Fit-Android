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

    private val viewModel: FindIdViewModel by sharedViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        binding.lifecycleOwner = this

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.init()
    }

}