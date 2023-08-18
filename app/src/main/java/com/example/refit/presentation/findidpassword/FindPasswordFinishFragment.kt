package com.example.refit.presentation.findidpassword

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.refit.MainActivity
import com.example.refit.R
import com.example.refit.databinding.FragmentFindPasswordFinishBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigate

class FindPasswordFinishFragment : BaseFragment<FragmentFindPasswordFinishBinding>(R.layout.fragment_find_password_finish) {

    val args: FindPasswordFinishFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.findPasswordFinishEmail.text = args.email

        binding.findPasswordBtn.setOnClickListener(){
            requireActivity().finish()
            val restartIntent = Intent(context, MainActivity::class.java)
            startActivity(restartIntent)
        }
    }
}