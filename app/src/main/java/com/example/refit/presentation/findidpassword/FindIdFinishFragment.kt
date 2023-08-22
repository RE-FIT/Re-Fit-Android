package com.example.refit.presentation.findidpassword

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.navArgs
import com.example.refit.MainActivity
import com.example.refit.R
import com.example.refit.databinding.FragmentFindIdFinishBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.common.NavigationUtil.navigateUp
import com.example.refit.presentation.findidpassword.viewModel.FindIdPasswordViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FindIdFinishFragment : BaseFragment<FragmentFindIdFinishBinding>(R.layout.fragment_find_id_finish) {

    private val vm: FindIdPasswordViewModel by sharedViewModel()
    val args: FindIdFinishFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.vm = vm

        val id = args.id.toString()
        binding.id = id

        //로그인 페이지 이동
        binding.btnFindIdBtn.setOnClickListener(){
            requireActivity().finish()
            val restartIntent = Intent(context, MainActivity::class.java)
            startActivity(restartIntent)
        }

        //비밀번호 찾기 이동
        binding.findIdFindPwBtn.setOnClickListener(){
            navigateUp()
        }
    }
}