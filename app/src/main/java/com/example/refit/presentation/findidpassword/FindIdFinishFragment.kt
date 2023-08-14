package com.example.refit.presentation.findidpassword

import android.os.Bundle
import android.view.View
import com.example.refit.R
import com.example.refit.databinding.FragmentFindIdFinishBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.findidpassword.viewModel.FindIdPasswordViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FindIdFinishFragment : BaseFragment<FragmentFindIdFinishBinding>(R.layout.fragment_find_id_finish) {

    private val vm: FindIdPasswordViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.vm = vm

        //아이디 찾기 이동
        binding.btnFindIdBtn.setOnClickListener(){
            navigate(R.id.action_findIdFinishFragment_to_findIdPasswordFragment)
        }

        //비밀번호 찾기 이동
        binding.findIdFindPwBtn.setOnClickListener(){
            navigate(R.id.action_findIdFinishFragment_to_findIdPasswordFragment)
        }


    }

}