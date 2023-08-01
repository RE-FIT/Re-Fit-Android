package com.example.refit.presentation.community.viewmodel

import android.annotation.SuppressLint
import android.icu.text.DecimalFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.model.community.PostDTO
import com.example.refit.data.repository.community.CommunityRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber
import java.io.File
import java.lang.Exception

class CommunityAddPostViewModel(
    private val repository: CommunityRepository,
    private val ds: TokenStore
) : ViewModel() {

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

    // 이미지 개수별 가시성
    private val _isFilledImageValues: List<MutableLiveData<Boolean>> =
        (0 until 5).map { MutableLiveData<Boolean>() }
    val isFilledImageValues: List<LiveData<Boolean>>
        get() = _isFilledImageValues

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

    /////////////////////////////////////////////// 코드 리팩토링 중 ////////////////////////////////////////////////
    // VALUE - 글 타입(0), 거래 방식(1), 배송비(2), 카테고리(3), 사이즈(4), 추천 성별(5), 가격(6)
    private val _postValue: List<MutableLiveData<Int>> = List(7) { MutableLiveData<Int>() }
    val postValue: List<LiveData<Int>>
        get() = _postValue


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

    // 사용자 주소 처리
    private val _postCode: MutableLiveData<String> = MutableLiveData<String>()
    val postCode: LiveData<String>
        get() = _postCode

    // 배송비 특성 (포함 false / 별도 true)
    private val _isSFExclude: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isSFExclude: LiveData<Boolean>
        get() = _isSFExclude

    // 필수 입력 항목들 값 채워짐 여부
    // 이미지(0), 글 제목(1), 추천 성별(2), 카테고리(3), 사이즈(4), 거래 방식(직/배-5), 가격(6), 상세 설명(7)
    private val _isFilledValue: List<MutableLiveData<Boolean>> =
        List(8) { MutableLiveData<Boolean>() }
    val isFilledValue: List<LiveData<Boolean>>
        get() = _isFilledValue


    private val _isFilledFee: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFilledFee: LiveData<Boolean>
        get() = _isFilledFee

    private val _isFilledDialogEditSF: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFilledDialogEditSF: LiveData<Boolean>
        get() = _isFilledDialogEditSF

// TODO 배송비 옵션 까지만 설정해둔 상태 밑으로 더 추가해줘야 함

    fun checkTransactionType(selectedType: String, typeList: List<String>) {
        when (selectedType) {
            typeList[0] -> { // 나눔
                _postValue[0].value = 0
                _postValue[6].value = 0
                _isFilledValue[6].value = true
            }

            else -> { // 판매
                _postValue[0].value = 1
            }
        }
        _isTransactionMethodChip.value = true
        setVisiblePriceStatus(false, 2)
        setVisibleFeeStatus(false)
        setVisibleRegionStatus(false)
    }


    fun selectTransactionMethod(itemType: String) {
        when (itemType) {
            "배송" -> {
                _postValue[1].value = 1
                postValue[0].value?.let { setVisiblePriceStatus(true, it) }
                setVisibleFeeStatus(true)
                setVisibleRegionStatus(false)
            }

            "직거래" -> {
                _postValue[1].value = 0
                setVisibleRegionStatus(true)
                postValue[0].value?.let { setVisiblePriceStatus(true, it) }
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
        if(value.isNotEmpty()) {
            _postValue[6].value = value.toInt()
        }
    }

    @SuppressLint("TimberArgCount")
    fun setVisibleRegionStatus(status: Boolean) {
        _isVisibleRegionStatus.value = status
        if (status) Timber.d("직거래 체크")
        else Timber.d("false")
    }

    fun setPostCode(data: String) {
        _postCode.value = data
        Timber.d("우편 API 동작 테스트 : $data")
    }

    fun setFilledStatus(type: Int, status: Boolean, value: String) {
        when (type) {
            0 -> _isFilledValue[0].value = status // 이미지
            1 -> _isFilledValue[1].value = status // 글 제목
            2 -> {
                _isFilledValue[2].value = status // 추천 성별
                _postValue[5].value = conversionTextToType(5, value)
            }

            3 -> {
                _isFilledValue[3].value = status // 카테고리
                _postValue[3].value = conversionTextToType(3, value)
            }

            4 -> {
                _isFilledValue[4].value = status // 사이즈
                _postValue[4].value = conversionTextToType(4, value)
            }

            5 -> _isFilledValue[5].value = status // 거래 방식(직/배)
            6 -> _isFilledValue[6].value = status // 가격
            7 -> _isFilledValue[7].value = status // 상세 설명
            8 -> _isFilledFee.value = status
            9 -> _isFilledDialogEditSF.value = status
            10 -> _isSFExclude.value = status
        }
    }

    fun setShippingFee(value: Int) {
        _postValue[2].value = value
    }

    fun setFilledImage(num: Int) {
        for (i in 0 until 5) {
            when {
                i < num -> _isFilledImageValues[i].value = true
                else -> _isFilledImageValues[i].value = false
            }
        }
    }

    private fun conversionTextToType(itemType: Int, value: String): Int {
        var type = Integer.MIN_VALUE
        when (itemType) {
            3 -> when (value) {
                "상의" -> type = 0
                "하의" -> type = 1
                "아우터" -> type = 2
                "원피스" -> type = 3
                "신발" -> type = 4
                "악세사리" -> type = 5
            }

            4 -> when (value) {
                "XS" -> type = 0
                "S" -> type = 1
                "M" -> type = 2
                "L" -> type = 3
                "XL" -> type = 4
            }

            5 -> when (value) {
                "여성복" -> type = 0
                "남성복" -> type = 1
            }


        }
        return type
    }

    fun getDecimalFormat(number: String): String {
        val intNumber = number.toIntOrNull() ?: 0
        val decimalFormat = DecimalFormat("#,###")
        return decimalFormat.format(intNumber)
    }

    fun createPost(
        title: String,
        detail: String,
        images: List<File>
    ) = viewModelScope.launch {
        val token = ds.getAccessToken().first()
        if (images == null || images.isEmpty()) {
            Timber.d("이미지 파일이 없습니다.")
            return@launch
        }
        try {

            val gender = _postValue[5].value ?: 0
            val postType = _postValue[0].value ?: 0
            val price = _postValue[6].value ?: 0
            val category = _postValue[3].value ?: 0
            val size = _postValue[4].value ?: 0
            val deliveryType = _postValue[1].value ?: 0
            val deliveryFee = _postValue[2].value ?: 0
            val sido = "서울시"
            val sigungu = "중랑구"
            val bname = "묵동"
            val bname2 = ""


            // 나눔 && 직거래
            var response: Call<ResponseBody> = repository.createPostShareNDt(
                token, title, gender, postType, category, size, deliveryType, detail, sido, sigungu, bname, bname2, images )

            when (postType) {
                // 나눔
                0 -> {
                    when (deliveryType) {
                        // 직거래
                        0 -> {
                            Timber.d("나눔 & 직거래")
                        }
                        // 배송
                        1 -> {
                            response = repository.createPostShareNDelivery(
                                token, title, gender, postType, category, size, deliveryType, deliveryFee, detail, images)
                            Timber.d("나눔 & 배송")
                        }
                    }
                }
                // 판매
                1 -> {
                    when (deliveryType) {
                        0 -> {
                            response = repository.createPostSaleNDt(token, title, gender, postType, price, category, size, deliveryType, detail, sido, sigungu, bname, bname2, images)
                            Timber.d("판매 & 직거래")
                        }
                        1 -> {
                            response = repository.createPostSaleNDelivery(token, title, gender, postType, price, category, size, deliveryType, deliveryFee, detail, images)
                            Timber.d("판매 & 나눔")
                        }
                    }
                }
            }


            Timber.d("title: $title\ngender: $gender \npostType: $postType\nprice: $price\n" +
                    "category: $category\nsize: $size\ndeliveryType: $deliveryType\n" +
                    "deliveryFee: $deliveryFee\ndetail: $detail\nimages: ${images.toString()}")
           // val response = repository.createPost(token, title, 0, 0, 0, 0, 0, 10000, detail, images)

            response.enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        Timber.d("COMMUNITY POST API 호출 성공")
                    } else {
                        try {
                            val errorBody = response.errorBody()
                            val errorCode = response.code()

                            if (errorBody != null) {
                                val errorJson = JSONObject(errorBody.string())
                                val errorMessage = errorJson.optString("message")
                                val errorCodeFromJson = errorJson.optInt("code")

                                Timber.d("API 호출 실패: $errorCodeFromJson / $errorMessage")
                            } else Timber.d("COMMUNITY POST API 호출 실패: $errorCode")
                        } catch (e: JSONException) {
                            Timber.d("error response failed : ${e.message}")
                        }

                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Timber.d("RESPONSE FAILURE")
                }
            })
        } catch (e: Exception) {
            Timber.d("커뮤니티 글 등록 과정 오류 발생: $e")
        }
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
        for (item in _isFilledValue) {
            item.value = false
        }
        _isFilledFee.value = false
        _isFilledDialogEditSF.value = false
        _isSFExclude.value = false
        _isSFPriceInputCompleted.value = false
        _isSFPriceInputCompleted.value = false
    }
}