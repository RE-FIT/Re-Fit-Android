package com.example.refit.presentation.community.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.refit.data.repository.community.CommunityRepository

class CommunityAddPostViewModel (
    private val communityRepository: CommunityRepository
): ViewModel() {

    private val _isTransactionMethodChip: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isTransactionMethodChip: LiveData<Boolean>
        get() = _isTransactionMethodChip

    private val _isClickedOptionRG: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isClickedOptionRG: LiveData<Boolean>
        get() = _isClickedOptionRG

    private val _isClickedOptionCategory: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isClickedOptionCategory: LiveData<Boolean>
        get() = _isClickedOptionCategory

    private val _isClickedOptionSize: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isClickedOptionSize: LiveData<Boolean>
        get() = _isClickedOptionSize

    private val _isClickedOptionTM: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isClickedOptionTM: LiveData<Boolean>
        get() = _isClickedOptionTM

    // 가격 정보 카드뷰 가시성
    private val _isVisiblePriceStatus: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isVisiblePriceStatus: LiveData<Boolean>
        get() = _isVisiblePriceStatus

    // 배송비 카드뷰 가시성
    private val _isVisibleFeeStatus: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isVisibleFeeStatus: LiveData<Boolean>
        get() = _isVisibleFeeStatus

    // 거래 희망 장소 카드뷰 가시성
    private val _isVisibleRegionStatus: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isVisibleRegionStatus: LiveData<Boolean>
        get() = _isVisibleRegionStatus


    // 거래 방식-1 나눔(1), 판매(2)
    private val _selectedType: MutableLiveData<Int> = MutableLiveData<Int>()
    val selectedType: LiveData<Int>
        get() = _selectedType

    // 거래 방식-2 배송(1), 직거래(2)
    private val _selectedTMType: MutableLiveData<Int> = MutableLiveData<Int>()
    val selectedTMType: LiveData<Int>
        get() = _selectedTMType

    // 거래 방식-3 (가격 정보) : 입력 받지 않음 (false), 입력 예정 (true)
    private val _priceCategory: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val priceCategory: LiveData<Boolean>
        get() = _priceCategory


    fun checkTransactionType(selectedType: String, typeList: List<String>) {
        when (selectedType) {
            typeList[0] -> {
                _selectedType.value = 1
            }
            else -> {
                _selectedType.value = 2
            }
        }
        _isTransactionMethodChip.value = true
        setVisiblePriceStatus(false)
        setVisibleFeeStatus(false)
        setVisibleRegionStatus(false)
    }


    fun selectTransactionMethod(itemType: String) {
        when (itemType) {
            "배송" -> {
                _selectedTMType.value = 1
                setVisiblePriceStatus(true)
                setVisibleFeeStatus(true)
                setVisibleRegionStatus(false)
                // TODO 나눔 판매 구분에 따라 가격 창 열어줘야 함
            }
            "직거래" -> {
                _selectedTMType.value = 2
                setVisibleRegionStatus(true)
                // TODO 나눔일 경우에는 가격 창 열면 안 됨 전체적으로 여기 처리 다시 하기
                // TODO 판매 - 직거래는 가격 입력 받아야 함
                setVisiblePriceStatus(false)
                setVisibleFeeStatus(false)
            }
        }
    }

    fun setClickedOptionRG(clicked: Boolean) {
        _isClickedOptionRG.value = clicked
    }

    fun setClickedOptionCategory(clicked: Boolean) {
        _isClickedOptionCategory.value = clicked
    }

    fun setClickedOptionSize(clicked: Boolean) {
        _isClickedOptionSize.value = clicked
    }

    fun setClickedOptionTM(clicked: Boolean) {
        _isClickedOptionTM.value = clicked
    }

    fun setVisiblePriceStatus(status: Boolean) {
        _isVisiblePriceStatus.value = status
        _isVisibleRegionStatus.value = status
    }

    fun setVisibleFeeStatus(status: Boolean) {
        _isVisibleFeeStatus.value = status
    }

    fun setVisibleRegionStatus(status: Boolean) {
        _isVisibleRegionStatus.value = status
    }

    fun initAllStatus() {
        _isTransactionMethodChip.value = false
        _isClickedOptionRG.value = false
        _isClickedOptionCategory.value = false
        _isClickedOptionSize.value = false
    }
}