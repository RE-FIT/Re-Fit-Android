package com.example.refit.presentation.closet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.refit.data.repository.colset.ClosetRepository
import java.util.Calendar

class ClosetViewModel(private val repository: ClosetRepository): ViewModel() {

    // 항목별 컨테이너 상태 값

    private val _isValidInvalidSeasonConfirm: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isValidInvalidSeasonConfirm: LiveData<Boolean>
        get() = _isValidInvalidSeasonConfirm

    private val _isValidShowingWearingGoal: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isValidShowingWearingGoal: LiveData<Boolean>
        get() = _isValidShowingWearingGoal

    private val _isNegativeInvalidSeasonConfirm: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isNegativeInvalidSeasonConfirm: LiveData<Boolean>
        get() = _isNegativeInvalidSeasonConfirm

    private val _isValidShowingRecommendWearing: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isValidShowingRecommendWearing: LiveData<Boolean>
        get() = _isValidShowingRecommendWearing

    // 컨테이너 내 뷰 상태 값

    private val _isFocusPopup: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFocusPopup: LiveData<Boolean>
        get() = _isFocusPopup

    private val _isFocusEditText: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFocusEditText: LiveData<Boolean>
        get() = _isFocusEditText

    private val _selectedMonthOption: MutableLiveData<Int> = MutableLiveData<Int>()
    val selectedMonthOption: LiveData<Int>
        get() = _selectedMonthOption

    private val _selectedWearingNumberOption: MutableLiveData<Int> = MutableLiveData<Int>()
    val selectedWearingNumberOption: LiveData<Int>
        get() = _selectedWearingNumberOption

    private val _recommendWearingNumberOfMonth: MutableLiveData<Int> = MutableLiveData<Int>()
    val recommendWearingNumberOfMonth: LiveData<Int>
        get() = _recommendWearingNumberOfMonth

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
        initRecommendWearingStatus(false)
        initInvalidSeasonConfirmStatus(!isValidSeason)
        initNegativeInvalidSeasonConfirmStatus(false)
    }

    fun checkInvalidSeasonConfirmResponse(selectedResponse: String, confirmResponse: List<String>) {
        val isPositive = when(selectedResponse) {
            confirmResponse[0] -> true
            else -> false
        }
        initClothWearingGoalOptionStatus(isPositive)
        initRecommendWearingStatus(false)
        initNegativeInvalidSeasonConfirmStatus(!isPositive)
    }

    fun setWearingGoalMonthOptionStatus(status: Boolean, option: String) {
        _isFocusPopup.value = status
        _selectedMonthOption.value = option[0].toString().toInt()
        if(_selectedWearingNumberOption.value != null && _selectedWearingNumberOption.value!! > 0) {
            initRecommendWearingStatus(true)
        }
    }

    fun setWearingGoalNumberOptionStatus(status: Boolean) {
        _isFocusEditText.value = status
    }

    fun handleInputCompleteWearingGoalNumberOption(text: String) {
        if(text.isEmpty()) {
            if(_isValidShowingRecommendWearing.value == true) {
                initRecommendWearingStatus(false)
                _selectedWearingNumberOption.value = 0
            }
        } else {
            _selectedWearingNumberOption.value = text.toInt()
            if(_selectedMonthOption.value != null && _selectedMonthOption.value!! > 0) {
                initRecommendWearingStatus(true)
            }
        }
    }

    private fun initClothWearingGoalOptionStatus(status: Boolean) {
        _isValidShowingWearingGoal.value = status
        _selectedWearingNumberOption.value = 0
        _selectedMonthOption.value = 0
        _isFocusPopup.value = false
        _isFocusEditText.value = false
    }

    private fun initRecommendWearingStatus(status: Boolean) {
        _isValidShowingRecommendWearing.value = status
    }

    private fun initInvalidSeasonConfirmStatus(status: Boolean) {
        _isValidInvalidSeasonConfirm.value = status
    }

    private fun initNegativeInvalidSeasonConfirmStatus(status: Boolean) {
        _isNegativeInvalidSeasonConfirm.value = status
    }

    fun initAllStatus() {
        initInvalidSeasonConfirmStatus(false)
        initClothWearingGoalOptionStatus(false)
        initRecommendWearingStatus(false)
        initNegativeInvalidSeasonConfirmStatus(false)
    }

}