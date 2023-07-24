package com.example.refit.presentation.closet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.refit.R
import com.example.refit.databinding.FragmentQuizBinding
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.common.WindowUtil
import com.example.refit.presentation.common.WindowUtil.setStatusBarColor

class QuizFragment : BaseFragment<FragmentQuizBinding>(R.layout.fragment_quiz) {



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.default_dark)
        handleClickToolBarkNavigation()

    }

    private fun handleClickToolBarkNavigation() {
        binding.tbForestQuizTopContainer.setNavigationOnClickListener {
            navigate(R.id.action_quizFragment_to_forestFragment);
        }
    }
}