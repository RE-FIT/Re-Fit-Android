package com.example.refit.presentation.closet.viewmodel

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.refit.R
import com.example.refit.data.repository.colset.ClosetRepository
import java.util.Calendar

class ClosetViewModel(private val repository: ClosetRepository): ViewModel() {

    private val _isValidInvalidSeasonConfirm: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isValidInvalidSeasonConfirm: LiveData<Boolean>
        get() = _isValidInvalidSeasonConfirm

    private val _isValidShowingWearingGoal: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isValidShowingWearingGoal: LiveData<Boolean>
        get() = _isValidShowingWearingGoal

    private val _isNegativeInvalidSeasonConfirm: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isNegativeInvalidSeasonConfirm: LiveData<Boolean>
        get() = _isNegativeInvalidSeasonConfirm



    fun checkSeasonValidation(selectedSeason: String, resources: Resources) {
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
        val isValidSeason: Boolean =
            when (selectedSeason) {
                resources.getString(R.string.cloth_register_category_wearing_season_spring) -> {
                    currentMonth in 3..5 || currentMonth in 9..11
                }

                resources.getString(R.string.cloth_register_category_wearing_season_summer) -> {
                    currentMonth in 6..8
                }

                else -> {
                    currentMonth in 1..2 || currentMonth == 12
                }
            }
        _isValidShowingWearingGoal.value = isValidSeason
        _isValidInvalidSeasonConfirm.value = !isValidSeason
        _isNegativeInvalidSeasonConfirm.value = false
    }

    fun checkInvalidSeasonConfirmResponse(selectedResponse: String, resources: Resources) {
        val isPositive = when(selectedResponse) {
            resources.getString(R.string.cloth_register_wearing_season_confirm_positive) -> true
            else -> false
        }
        _isValidShowingWearingGoal.value = isPositive
        _isNegativeInvalidSeasonConfirm.value = !isPositive
    }

    private fun initContainerStatus() {
        _isValidInvalidSeasonConfirm.value = false
        _isValidShowingWearingGoal.value = false
        _isNegativeInvalidSeasonConfirm.value = false
    }

}