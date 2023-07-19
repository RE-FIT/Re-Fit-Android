package com.example.refit.presentation.community.viewmodel

import android.annotation.SuppressLint
import android.icu.text.DecimalFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.refit.data.repository.community.CommunityRepository
import timber.log.Timber

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


    // 거래 방식-1 나눔(0), 판매(1)
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

    // 가격 입력창 활성/비활성 여부 (나눔 - false, 판매 - true)
    private val _isPriceInputEnabled: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isPriceInputEnabled: LiveData<Boolean>
        get() = _isPriceInputEnabled

    // 다이얼로그 내부 배송비 입력 완료 여부
    private val _isSFPriceInputCompleted: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isSFPriceInputCompleted: LiveData<Boolean>
        get() = _isSFPriceInputCompleted

    private val _isPriceInputCompleted: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isPriceInputCompleted: LiveData<Boolean>
        get() = _isPriceInputCompleted


    // 배송비 특성 (포함 false / 별도 true)
    private val _isSFExclude: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isSFExclude: LiveData<Boolean>
        get() = _isSFExclude

    // 배송비 별도 시 배송비
    private val _shippingFee: MutableLiveData<Int> = MutableLiveData<Int>()
    val shippingFee: LiveData<Int>
        get() = _shippingFee

    // 필수 입력 항목들 값 채워짐 여부
    private val _isFilledImage: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFilledImage: LiveData<Boolean>
        get() = _isFilledImage

    private val _isFilledTitle: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFilledTitle: LiveData<Boolean>
        get() = _isFilledTitle

    private val _isFilledRG: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFilledRG: LiveData<Boolean>
        get() = _isFilledRG

    private val _isFilledCategory: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFilledCategory: LiveData<Boolean>
        get() = _isFilledCategory

    private val _isFilledSize: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFilledSize: LiveData<Boolean>
        get() = _isFilledSize

    private val _isFilledTMChip: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFilledTMChip: LiveData<Boolean>
        get() = _isFilledTMChip

    private val _isFilledTM: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFilledTM: LiveData<Boolean>
        get() = _isFilledTM

    private val _isFilledPrice: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFilledPrice: LiveData<Boolean>
        get() = _isFilledPrice

    private val _isFilledFee: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFilledFee: LiveData<Boolean>
        get() = _isFilledFee

    private val _isFilledDialogEditSF: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFilledDialogEditSF: LiveData<Boolean>
        get() = _isFilledDialogEditSF

// TODO 배송비 옵션 까지만 설정해둔 상태 밑으로 더 추가해줘야 함

    fun checkTransactionType(selectedType: String, typeList: List<String>) {
        when (selectedType) {
            typeList[0] -> {
                _selectedType.value = 0
            }
            else -> {
                _selectedType.value = 1
            }
        }
        _isTransactionMethodChip.value = true
        setVisiblePriceStatus(false, 2)
        setVisibleFeeStatus(false)
        setVisibleRegionStatus(false)
    }


    fun selectTransactionMethod(itemType: String) {
        setFilledStatus(6, true)
        when (itemType) {
            "배송" -> {
                _selectedTMType.value = 1
                selectedType.value?.let { setVisiblePriceStatus(true, it) }
                setVisibleFeeStatus(true)
                setVisibleRegionStatus(false)
            }
            "직거래" -> {
                _selectedTMType.value = 2
                setVisibleRegionStatus(true)
                // TODO 판매 - 직거래는 가격 입력 받아야 함
                selectedType.value?.let { setVisiblePriceStatus(true, it) }
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

    fun setVisiblePriceStatus(status: Boolean, type: Int) {
        _isVisiblePriceStatus.value = status
        when (type) {
            0 -> _isPriceInputEnabled.value = false
            1 -> _isPriceInputEnabled.value = true
        }
    }

    fun setVisibleFeeStatus(status: Boolean) {
        _isVisibleFeeStatus.value = status
    }

    fun setSFPriceInputCompleted(value: String) {
        _isSFPriceInputCompleted.value = value.isNotEmpty()
    }

    fun setPriceInputCompleted(value: String) {
        _isPriceInputCompleted.value = value.isNotEmpty()
    }

    @SuppressLint("TimberArgCount")
    fun setVisibleRegionStatus(status: Boolean) {
        _isVisibleRegionStatus.value = status
        if(status) Timber.d("직거래 체크")
        else Timber.d("false")
    }

    fun setFilledStatus(type: Int, status: Boolean) {
        when (type) {
            0 -> _isFilledImage.value = status
            1 -> _isFilledTitle.value = status
            2 -> _isFilledRG.value = status
            3 -> _isFilledCategory.value = status
            4 -> _isFilledSize.value = status
            5 -> _isFilledTMChip.value = status
            6 -> _isFilledTM.value = status
            7 -> _isFilledPrice.value = status
            8 -> _isFilledFee.value = status
            9 -> _isFilledDialogEditSF.value = status
            10 -> _isSFExclude.value = status
        }
    }

    fun setShippingFee(value: Int) {
        _shippingFee.value = value
    }

    fun getDecimalFormat(number: String): String {
        val intNumber = number.toIntOrNull() ?: 0
        val decimalFormat = DecimalFormat("#,###")
        return decimalFormat.format(intNumber)
    }

    fun initAllStatus() {
        _isTransactionMethodChip.value = false
        _isClickedOptionRG.value = false
        _isClickedOptionCategory.value = false
        _isClickedOptionSize.value = false
    }

    fun initTransactionStatus() {
        _isVisibleRegionStatus.value = false
        _isVisiblePriceStatus.value = false
        _isVisibleFeeStatus.value = false
    }

    fun initFilledState() {
        _isFilledImage.value = false
        _isFilledTitle.value = false
        _isFilledRG.value = false
        _isFilledCategory.value = false
        _isFilledSize.value = false
        _isFilledTMChip.value = false
        _isFilledTM.value = false
        _isFilledPrice.value = false
        _isFilledFee.value = false
        _isFilledDialogEditSF.value = false
        _isSFExclude.value = false
        _isSFPriceInputCompleted.value = false
        _isSFPriceInputCompleted.value = false
    }
}