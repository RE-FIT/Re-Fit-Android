package com.example.refit.presentation.closet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.refit.R
import com.example.refit.databinding.FragmentQuizBinding
import com.example.refit.presentation.closet.viewmodel.ForestViewModel
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.common.NavigationUtil.navigateUp
import com.example.refit.presentation.common.WindowUtil
import com.example.refit.presentation.common.WindowUtil.setNavigationBarColor
import com.example.refit.presentation.common.WindowUtil.setStatusBarColor
import com.example.refit.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class QuizFragment : BaseFragment<FragmentQuizBinding>(R.layout.fragment_quiz) {

    private val forestViewModel: ForestViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(R.color.default_dark)
        forestViewModel.getQuiz()
        handleClickToolBarkNavigation()
        handleAnswerRequestStatus()
    }

    private fun handleClickToolBarkNavigation() {
        binding.tbForestQuizTopContainer.setNavigationOnClickListener {
            navigate(R.id.action_quizFragment_to_forestFragment)
        }
    }

    private fun handleAnswerRequestStatus() {
        forestViewModel.isRequestExit.observe(viewLifecycleOwner, EventObserver {isRequestExit ->
            if(isRequestExit) {
                navigateUp()
            }
        })
    }
}