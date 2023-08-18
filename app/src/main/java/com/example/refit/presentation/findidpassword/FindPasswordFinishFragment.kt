package com.example.refit.presentation.findidpassword

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.refit.R
import com.example.refit.databinding.FragmentFindPasswordFinishBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigate

class FindPasswordFinishFragment : BaseFragment<FragmentFindPasswordFinishBinding>(R.layout.fragment_find_password_finish) {

    val args: FindPasswordFinishFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.findPasswordFinishEmail.text = args.email

        binding.findPasswordBtn.setOnClickListener(){
            Toast.makeText(context, "이메일로 임시 비밀번호가 발급되었습니다.", Toast.LENGTH_LONG).show()
            navigate(R.id.action_findPasswordFinishFragment_to_signInFragment)
        }
    }
}