package com.example.refit.presentation.closet

import android.os.Bundle
import android.view.View
import com.example.refit.R
import com.example.refit.databinding.FragmentQuizBinding
import com.example.refit.presentation.closet.viewmodel.ForestViewModel
import com.example.refit.presentation.common.BaseFragment
import com.example.refit.presentation.common.NavigationUtil.navigate
import com.example.refit.presentation.common.WindowUtil.setStatusBarColor
import com.example.refit.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class QuizFragment : BaseFragment<FragmentQuizBinding>(R.layout.fragment_quiz) {

    private val forestViewModel: ForestViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = forestViewModel
        setStatusBarColor(R.color.default_dark)
        forestViewModel.getQuiz()
        handleClickToolBarkNavigation()
        handleAnswerRequestStatus()
        handleRequestAnswer()
    }

    private fun handleClickToolBarkNavigation() {
        binding.tbForestQuizTopContainer.setNavigationOnClickListener {
            navigate(R.id.action_quizFragment_to_forestFragment)
        }
    }

    private fun handleAnswerRequestStatus() {
        forestViewModel.isRequestExit.observe(viewLifecycleOwner, EventObserver {isRequestExit ->
            if(isRequestExit) {
                navigate(R.id.action_quizFragment_to_forestFragment)
            }
        })
    }

    private fun handleRequestAnswer() {
        forestViewModel.isRequestAnswer.observe(viewLifecycleOwner, EventObserver {isRequestAnswer ->
                binding.isRequestAnswer = isRequestAnswer
        })
    }
}