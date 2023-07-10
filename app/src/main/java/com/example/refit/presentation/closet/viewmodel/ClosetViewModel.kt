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

    private val _isFocusPopup: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFocusPopup: LiveData<Boolean>
        get() = _isFocusPopup

    private val _isFocusEditText: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFocusEditText: LiveData<Boolean>
        get() = _isFocusEditText

    private val _selectedMonthOption: MutableLiveData<String> = MutableLiveData<String>()
    val selectedMonthOption: LiveData<String>
        get() = _selectedMonthOption




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
        initClothWearingGoalOptionStatus(isValidSeason)
        _isValidInvalidSeasonConfirm.value = !isValidSeason
        _isNegativeInvalidSeasonConfirm.value = false
    }

    fun checkInvalidSeasonConfirmResponse(selectedResponse: String, resources: Resources) {
        val isPositive = when(selectedResponse) {
            resources.getString(R.string.cloth_register_wearing_season_confirm_positive) -> true
            else -> false
        }
        initClothWearingGoalOptionStatus(isPositive)
        _isNegativeInvalidSeasonConfirm.value = !isPositive
    }

    fun setWearingGoalMonthOptionStatus(status: Boolean, option: String) {
        _isFocusPopup.value = status
        _selectedMonthOption.value = option
    }

    fun setWearingGoalNumberOptionStatus(status: Boolean) {
        _isFocusEditText.value = status
    }

    private fun initClothWearingGoalOptionStatus(status: Boolean) {
        _isValidShowingWearingGoal.value = status
        _isFocusPopup.value = false
        _isFocusEditText.value = false
    }
}