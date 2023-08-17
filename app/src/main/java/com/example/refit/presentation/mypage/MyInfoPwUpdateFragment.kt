package com.example.refit.presentation.mypage

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.refit.MainActivity
import com.example.refit.R
import com.example.refit.data.model.mypage.PasswordUpdateRequest
import com.example.refit.databinding.FragmentMyInfoPwUpdateBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.CustomSnackBar
import com.example.refit.presentation.common.DialogUtil.checkPwDialog
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.mypage.viewmodel.MyInfoViewModel
import com.example.refit.presentation.mypage.viewmodel.PwChangeViewModel
import com.example.refit.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class MyInfoPwUpdateFragment : BaseFragment<FragmentMyInfoPwUpdateBinding>(R.layout.fragment_my_info_pw_update) {

    private val vm: PwChangeViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = vm
        binding.lifecycleOwner = this

        //패스워드 변경 API 호출
        binding.btnPwUpdate.setOnClickListener {
            val pw = binding.currentPw.text.toString()
            val next = binding.newPw.text.toString()

            if(pw.isNotEmpty() && next.isNotEmpty()){
                vm.updatePassword(PasswordUpdateRequest(pw,next))
            }
        }

        //성공 처리
        vm.changeSuccess.observe(viewLifecycleOwner, EventObserver{
            requireActivity().finish()
            val restartIntent = Intent(context, MainActivity::class.java)
            startActivity(restartIntent)
        })

        //에러 처리
        vm.error.observe(viewLifecycleOwner, EventObserver{
            notifyPwIncorrectDialog()
        })
    }

    fun notifyPwIncorrectDialog() {
        checkPwDialog(
            resources.getString(R.string.pw_incorrect_title),
            resources.getString(R.string.pw_incorrect_content)
        ).show(requireActivity().supportFragmentManager, null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vm.init()
    }
}