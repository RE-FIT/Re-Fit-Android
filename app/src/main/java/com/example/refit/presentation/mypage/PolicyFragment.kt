package com.example.refit.presentation.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.refit.R
import com.example.refit.databinding.FragmentPolicyBinding
import com.example.refit.databinding.FragmentSettingBinding
import com.example.refit.databinding.FragmentTermsBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.signin.viewmodel.SignInViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PolicyFragment : BaseFragment<FragmentPolicyBinding>(R.layout.fragment_policy) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}