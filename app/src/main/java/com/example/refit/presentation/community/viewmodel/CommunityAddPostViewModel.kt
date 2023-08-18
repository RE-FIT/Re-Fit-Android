package com.example.refit.presentation.community.viewmodel

import android.annotation.SuppressLint
import android.icu.text.DecimalFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.model.community.PostDTODelivery
import com.example.refit.data.model.community.PostDTODt
import com.example.refit.data.model.community.PostResponse
import com.example.refit.data.repository.community.CommunityRepository
import com.example.refit.data.repository.community.PostDataRepository
import com.example.refit.util.Event
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.File

class CommunityAddPostViewModel(
    private val repository: CommunityRepository,
    private val ds: TokenStore
) : ViewModel() {

    private var _success = MutableLiveData<Event<Boolean>>()
    val success : LiveData<Event<Boolean>>
        get() = _success

    private val _postId: MutableLiveData<Int> = MutableLiveData<Int>()
    val postId: LiveData<Int>
        get() = _postId

    private val _isModifyPost: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isModifyPost: LiveData<Boolean>
        get() = _isModifyPost

    val postResponse: LiveData<PostResponse> = PostDataRepository.postResponse

    private val _photoUris: MutableLiveData<List<String>?> = MutableLiveData<List<String>?>()
    val photoUris: LiveData<List<String>?>
        get() = _photoUris

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

    // VALUE - 글 타입(0), 거래 방식(1), 배송비(2), 카테고리(3), 사이즈(4), 추천 성별(5), 가격(6)
    private val _postValue: List<MutableLiveData<Int>> = List(11) { MutableLiveData<Int>() }
    val postValue: List<LiveData<Int>>
        get() = _postValue

    // ADDRESS VALUE
    private val _postAddressValue: MutableLiveData<String> = MutableLiveData<String>()
    val postAddressValue: LiveData<String>
        get() = _postAddressValue

    // VALUE - 타이틀
    private val _postTitle: MutableLiveData<String> = MutableLiveData<String>()
    val postTitle: LiveData<String>
        get() = _postTitle

    // VALUE - 상세 설명
    private val _postDetail: MutableLiveData<String> = MutableLiveData<String>()
    val postDetail: LiveData<String>
        get() = _postDetail

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
    // 이미지(0), 글 제목(1), 추천 성별(2), 카테고리(3), 사이즈(4), 거래 방식(직/배-5), 상세 설명(6)
    private val _isFilledValue: List<MutableLiveData<Boolean>> =
        List(7) { MutableLiveData<Boolean>() }
    val isFilledValue: List<LiveData<Boolean>>
        get() = _isFilledValue

    // 직거래의 경우의 필수 항목 채워짐 여부: address
    val _isFilledAddress: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFilledAddress: LiveData<Boolean>
        get() = _isFilledAddress

    private val _isFilledPrice: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFilledPrice: LiveData<Boolean>
        get() = _isFilledPrice

    private val _isFilledFee: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFilledFee: LiveData<Boolean>
        get() = _isFilledFee

    private val _isFilledFeeStatus: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFIlledFeeStatus: LiveData<Boolean>
        get() = _isFilledFeeStatus

    private val _isFilledDialogEditSF: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFilledDialogEditSF: LiveData<Boolean>
        get() = _isFilledDialogEditSF

    // 등록에 필요한 모든 값이 채워졌는가?
    private val _isFilledAllOptions: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFilledAllOptions: LiveData<Boolean>
        get() = _isFilledAllOptions

    private val _photoLen: MutableLiveData<Int> = MutableLiveData<Int>()
    val photoLen: LiveData<Int>
        get() = _photoLen

    private val _token: MutableLiveData<String> = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token

    private val _postCodeValue: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val postCodeValue: LiveData<Boolean>
        get() = _postCodeValue

    // 수정 시에 이미지 변경 여부 확인
    private val _modifyImageStatus: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val modifyImageStatus: LiveData<Boolean>
        get() = _modifyImageStatus

    private val _textIfShoesOrAcc: MutableLiveData<String> = MutableLiveData<String>()
    val textIfShoesOrAcc: LiveData<String>
        get() = _textIfShoesOrAcc

    private val _updateStatus: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val updateStatus: LiveData<Boolean>
        get() = _updateStatus

    fun setPostId(id: Int) {
        _postId.value = id
    }

    fun setFeeStatus() {
        _isFilledFeeStatus.value = isFilledFee.value == true && (postValue[2].value == null || postValue[2].value == 0)
    }

    fun checkTransactionType(selectedType: String, typeList: List<String>) {
        when (selectedType) {
            typeList[0] -> { // 나눔
                _postValue[0].value = 0
                _postValue[6].value = 0
                _isFilledPrice.value = true
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

    fun setPostTitleAndDetail(title: String, detail: String) {
        _postTitle.value = title
        _postDetail.value = detail
    }

    fun setAddress(value: String) {
        _postAddressValue.value = value
        setFilledStatus(11, true, "")
    }
    fun setImageUris(value: List<String>) {
        _photoUris.value = value
        if(isModifyPost.value == true) _modifyImageStatus.value = true
        Timber.d("포토 uris: ${photoUris.value}")
    }

    fun setPhotoLen(size: Int) {
        _photoLen.value = size
    }

    ////////////////////////// 수정 시
    fun setValueIfModifyStatus(){
        Timber.d("postResponse NULL")
        if(postResponse.value != null) {
            Timber.d("postResponse !NULL")

            // 이미지 관련 처리
            _photoUris.value = postResponse.value?.imgUrls
            _photoLen.value = postResponse.value?.imgUrls?.size
            _isFilledValue[0].value = true
            for (i in 0 until _photoLen.value!!) {
                _isFilledImageValues[i].value = true
            }

            // 제목 관련 처리
            _postTitle.value = postResponse.value!!.title


            // 추천 착용 성별, 카테고리, 사이즈 관련 처리
            _isClickedOptionRG.value = true
            _isFilledValue[2].value = true
            _postValue[5].value = postResponse.value!!.gender

            _isClickedOptionCategory.value = true
            _isFilledValue[3].value = true
            _postValue[3].value = postResponse.value!!.category

            _isClickedOptionSize.value = true
            _isFilledValue[4].value = true
            _postValue[4].value = postResponse.value!!.size

            // 거래 방식(나눔/판매)
            _postValue[0].value = postResponse.value!!.postType
            _isClickedOptionTM.value = true

            // 거래 희망 방식 (직/배)
            _isFilledValue[5].value = true
            _postValue[1].value = postResponse.value!!.deliveryType

            if(_postValue[1].value == 0) {
                // 직거래일시, 주소 정보 처리
                _postAddressValue.value = postResponse.value!!.address.toString()
                _isFilledAddress.value = true
            } else {
                // 배송일 시, 배송비 관련 정보 처리
                _postValue[2].value = postResponse.value!!.deliveryFee
                _isFilledFee.value = true
            }

            // 가격
            _postValue[6].value = postResponse.value?.price
            _isFilledPrice.value = true

        }
    }

    fun setModifyOrNew(status: Boolean) {
        _isModifyPost.value = status
        if(status) {
            setValueIfModifyStatus()
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
        if (value.isNotEmpty()) {
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
        viewModelScope.launch (Dispatchers.Main) {
            if(data != null) {
                _postCode.value = data
                _postCodeValue.value = true
            }
            Timber.d("우편 API 동작 테스트 : $data")
        }
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
            6 -> _isFilledPrice.value = status // 가격
            7 -> _isFilledValue[6].value = status // 상세 설명
            8 -> _isFilledFee.value = status
            9 -> _isFilledDialogEditSF.value = status
            10 -> _isSFExclude.value = status
            11 -> _isFilledAddress.value = status
        }
        gaugeFilledStatus()
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

    fun gaugeFilledStatus() {
        val pt = _postValue[0].value ?: 0
        val dt = _postValue[1].value ?: 0
        var gauge: Boolean = true

        gauge = gauge && _isFilledValue.all { it.value == true }
        Timber.d("글 등록 가시성 value 리스트에서 $gauge")
        Timber.d("_isFilledValue 글 등록 가시성:\n이미지: ${_isFilledValue[0].value}\n" +
                "글 제목: ${_isFilledValue[1].value}\n" +
                "성별: ${_isFilledValue[2].value}\n" +
                "카테고리: ${_isFilledValue[3].value}\n" +
                "사이즈: ${_isFilledValue[4].value}\n" +
                "직배여부: ${_isFilledValue[5].value}\n" +
                "상세설명: ${_isFilledValue[6].value}")

        if (dt == 0) { // 직거래인 경우
            gauge = gauge && (_isFilledAddress.value == true)
            Timber.d("글 등록 가시성 직거래의 경우에서 $gauge")
        } else { // 배송인 경우
            gauge = gauge && (_isFilledFee.value == true)
            Timber.d("글 등록 가시성 배송의 경우에서 $gauge")
        }

        if (pt == 1) { // 판매인 경우
            // price 필수 입력 체크 추가
            gauge = gauge && (_isFilledPrice.value == true)
            Timber.d("글 등록 가시성 판매의 경우에서 $gauge")
        }

        _isFilledAllOptions.value = gauge
        Timber.d("글 등록 가시성 테스트: pt-$pt, dt:$dt, 총괄: ${_isFilledAllOptions.value}")
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
                "상세설명 입력" -> type = 5
            }

            5 -> when (value) {
                "여성복" -> type = 0
                "남성복" -> type = 1
            }


        }
        return type
    }

    fun conversionTypeToText(itemType: Int, value: Int): String {
        var text = ""
        when (itemType) {
            2 -> when (value) {
                0 -> text = "직거래"
                1 -> text = "배송"
            }
            3 -> when (value) {
                0 -> text = "상의"
                1 -> text = "하의"
                2 -> text = "아우터"
                3 -> text = "원피스"
                4 -> text = "신발"
                5 -> text = "악세사리"
            }
            4 -> when (value) {
                0 -> text = "XS"
                1 -> text = "S"
                2 -> text = "M"
                3 -> text = "L"
                4 -> text = "XL"
                5 -> text = "상세설명 입력"
            }
            5 -> when (value) {
                0 -> text = "여성복"
                1 -> text = "남성복"
            }
        }
        return text
    }

    fun gaugeShoesOrAcc(): Boolean {
        return (postValue[3].value == 4 || postValue[3].value == 5)
    }

    fun setTextIfShoesOrAcc(): String {
        if(isFilledValue[4].value == true) {
            if(postValue[4].value == 4 || postValue[4].value == 5) {
                _isFilledValue[4].value == true
                _textIfShoesOrAcc.value = "상세설명 입력"
            } else {
                _textIfShoesOrAcc.value = conversionTypeToText(4, postValue[4].value!!)
            }
        } else _textIfShoesOrAcc.value = "사이즈를 입력해주세요"
        return textIfShoesOrAcc.value.toString()
    }

    fun getDecimalFormat(number: String): String {
        val intNumber = number.toIntOrNull() ?: 0
        val decimalFormat = DecimalFormat("#,###")
        return decimalFormat.format(intNumber)
    }

    fun getToken() {
        viewModelScope.launch {
            _token.value = ds.getAccessToken().first()
        }
    }

    fun createPost(
        images: List<File>
    ) = viewModelScope.launch {
        val token = ds.getAccessToken().first()
        if (images.isEmpty()) {
            Timber.d("이미지 파일이 없습니다.")
            return@launch
        }
        try {

            val postDTO = when (_postValue[1].value ?: 0) {
                0 -> {
                    PostDTODt(
                        title = _postTitle.value ?: "",
                        gender = _postValue[5].value ?: 0,
                        postType = _postValue[0].value ?: 0,
                        price = _postValue[6].value ?: 0,
                        category = _postValue[3].value ?: 0,
                        size = _postValue[4].value ?: 5,
                        deliveryType = _postValue[1].value ?: 0,
                        deliveryFee = _postValue[2].value ?: 0,
                        detail = _postDetail.value ?: "",
                        address = _postAddressValue.value ?: "00시 00구 00동"
                    )
                }

                1 -> {
                    PostDTODelivery(
                        title = _postTitle.value ?: "",
                        gender = _postValue[5].value ?: 0,
                        postType = _postValue[0].value ?: 0,
                        price = _postValue[6].value ?: 0,
                        category = _postValue[3].value ?: 0,
                        size = _postValue[4].value ?: 5,
                        deliveryType = _postValue[1].value ?: 0,
                        deliveryFee = _postValue[2].value ?: 0,
                        detail = _postDetail.value ?: "",
                    )
                }

                else -> Timber.d("postDTO 생성 과정 오류 발생")
            }

            // PostDto 객체를 JSON 형태의 문자열로 변환
            val postDtoJson = Gson().toJson(postDTO)
            Timber.d("[POST] postDto to JSON : ${postDtoJson.toString()}")
            val postDtoRequestBody =
                postDtoJson.toRequestBody("application/json".toMediaTypeOrNull())

            var response: Call<ResponseBody> = repository.createPost(
                token, postDtoRequestBody, images
            )

            response.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val json = response.body()?.string()
                        Timber.d("COMMUNITY POST API 호출 성공 : $json")
                        _success.postValue(Event(true))
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

    fun modifyPostIncludeImage(
        images: List<File>
    ) = viewModelScope.launch {
        val token = ds.getAccessToken().first()
        if (images.isEmpty()) {
            Timber.d("이미지 파일이 없습니다.")
            return@launch
        }
        try {
            val postId = _postId.value ?: 0
            val postDTO = when (_postValue[1].value ?: 0) {
                0 -> {
                    PostDTODt(
                        title = _postTitle.value ?: "",
                        gender = _postValue[5].value ?: 0,
                        postType = _postValue[0].value ?: 0,
                        price = _postValue[6].value ?: 0,
                        category = _postValue[3].value ?: 0,
                        size = _postValue[4].value ?: 0,
                        deliveryType = _postValue[1].value ?: 0,
                        deliveryFee = _postValue[2].value ?: 0,
                        detail = _postDetail.value ?: "",
                        address = _postAddressValue.value ?: "00시 00구 00동"
                    )
                }

                1 -> {
                    PostDTODelivery(
                        title = _postTitle.value ?: "",
                        gender = _postValue[5].value ?: 0,
                        postType = _postValue[0].value ?: 0,
                        price = _postValue[6].value ?: 0,
                        category = _postValue[3].value ?: 0,
                        size = _postValue[4].value ?: 0,
                        deliveryType = _postValue[1].value ?: 0,
                        deliveryFee = _postValue[2].value ?: 0,
                        detail = _postDetail.value ?: "",
                    )
                }
                else -> Timber.d("postDTO 생성 과정 오류 발생")
            }

            // PostDto 객체를 JSON 형태의 문자열로 변환
            val postDtoJson = Gson().toJson(postDTO)
            Timber.d("[PUT] postDto to JSON : ${postDtoJson.toString()}")
            val postDtoRequestBody =
                postDtoJson.toRequestBody("application/json".toMediaTypeOrNull())

            var response: Call<ResponseBody> = repository.modifyPostIncludeImage(
                token, true, postId, postDtoRequestBody, images)


            response.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val json = response.body()?.string()
                        Timber.d("COMMUNITY PUT API 호출 성공 : $json")
                        _updateStatus.value = true
                    } else {
                        try {
                            val errorBody = response.errorBody()
                            val errorCode = response.code()

                            if (errorBody != null) {
                                val errorJson = JSONObject(errorBody.string())
                                val errorMessage = errorJson.optString("message")
                                val errorCodeFromJson = errorJson.optInt("code")

                                Timber.d("API 호출 실패: $errorCodeFromJson / $errorMessage")
                            } else Timber.d("COMMUNITY PUT API 호출 실패: $errorCode")
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
            Timber.d("커뮤니티 글 수정 과정 오류 발생: $e")
        }
    }

    fun modifyPost() = viewModelScope.launch {
        val token = ds.getAccessToken().first()
        try {
            val postId = _postId.value ?: 0
            val postDTO = when (_postValue[1].value ?: 0) {
                0 -> {
                    PostDTODt(
                        title = _postTitle.value ?: "",
                        gender = _postValue[5].value ?: 0,
                        postType = _postValue[0].value ?: 0,
                        price = _postValue[6].value ?: 0,
                        category = _postValue[3].value ?: 0,
                        size = _postValue[4].value ?: 0,
                        deliveryType = _postValue[1].value ?: 0,
                        deliveryFee = _postValue[2].value ?: 0,
                        detail = _postDetail.value ?: "",
                        address = _postAddressValue.value ?: "00시 00구 00동"
                    )
                }

                1 -> {
                    PostDTODelivery(
                        title = _postTitle.value ?: "",
                        gender = _postValue[5].value ?: 0,
                        postType = _postValue[0].value ?: 0,
                        price = _postValue[6].value ?: 0,
                        category = _postValue[3].value ?: 0,
                        size = _postValue[4].value ?: 0,
                        deliveryType = _postValue[1].value ?: 0,
                        deliveryFee = _postValue[2].value ?: 0,
                        detail = _postDetail.value ?: "",
                    )
                }
                else -> Timber.d("postDTO 생성 과정 오류 발생")
            }

            // PostDto 객체를 JSON 형태의 문자열로 변환
            val postDtoJson = Gson().toJson(postDTO)
            Timber.d("[PUT] postDto to JSON : ${postDtoJson.toString()}")
            val postDtoRequestBody =
                postDtoJson.toRequestBody("application/json".toMediaTypeOrNull())

            var response: Call<ResponseBody> = repository.modifyPost(
                token, false, postId, postDtoRequestBody)

            response.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val json = response.body()?.string()
                        Timber.d("COMMUNITY PUT API 호출 성공 : $json")
                        _updateStatus.value = true
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
            Timber.d("커뮤니티 글 수정 과정 오류 발생: $e")
        }
    }

    fun setPostCodeValue(value: Boolean) {
        _postCodeValue.value = value
    }

    fun initAllStatus() {
        _updateStatus.value = false
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
        _isFilledAllOptions.value = false

        for (item in _isFilledValue) {
            item.value = false
        }
        _isFilledAddress.value = false
        for (item in _isFilledImageValues) {
            item.value = false
        }

        _photoUris.value = null
        _modifyImageStatus.value = false
        _postCodeValue.value = false
        _postTitle.value = ""
        _photoLen.value = 0
        _isModifyPost.value = false
        _isFilledFee.value = false
        _isFilledDialogEditSF.value = false
        _isSFExclude.value = false
        _isSFPriceInputCompleted.value = false
        _isSFPriceInputCompleted.value = false
    }
}