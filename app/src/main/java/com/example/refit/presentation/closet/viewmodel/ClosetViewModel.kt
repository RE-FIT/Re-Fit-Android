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

    private val _selectedSeason: MutableLiveData<String> = MutableLiveData<String>()
    val selectedSeason: LiveData<String>
        get() = _selectedSeason



    fun checkSeasonValidation(selectedSeason: String, seasonList: List<String>) {
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
        val isValidSeason: Boolean =
            when (selectedSeason) {
                seasonList[0] -> {
                    _selectedSeason.value = "봄 · 가을"
                    currentMonth in 3..5 || currentMonth in 9..11
                }

                seasonList[1] -> {
                    _selectedSeason.value = "여름"
                    currentMonth in 6..8
                }

                else -> {
                    _selectedSeason.value = "겨울"
                    currentMonth in 1..2 || currentMonth == 12
                }
            }
        initClothWearingGoalOptionStatus(isValidSeason)
        _isValidInvalidSeasonConfirm.value = !isValidSeason
        _isNegativeInvalidSeasonConfirm.value = false
    }

    fun checkInvalidSeasonConfirmResponse(selectedResponse: String, confirmResponse: List<String>) {
        val isPositive = when(selectedResponse) {
            confirmResponse[0] -> true
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