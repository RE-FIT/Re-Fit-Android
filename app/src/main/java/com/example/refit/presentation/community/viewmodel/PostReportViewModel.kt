package com.example.refit.presentation.community.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.model.community.BlockDto
import com.example.refit.data.model.community.Member
import com.example.refit.data.model.community.ReportedUser
import com.example.refit.data.repository.community.CommunityRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.lang.Exception

class PostReportViewModel(
    private val repository: CommunityRepository,
    private val ds: TokenStore,
): ViewModel() {

    private val _userName: MutableLiveData<String> = MutableLiveData<String>()
    val userName: LiveData<String>
        get() = _userName

    // 신고 사유 선택 여부
    private val _isSelectReason: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isSelectReason : LiveData<Boolean>
        get() = _isSelectReason

    // 신고 사유 종류별 status
    private val _isSelectReasonFirst: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isSelectReasonFirst : LiveData<Boolean>
        get() = _isSelectReasonFirst

    private val _isSelectReasonSecond: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isSelectReasonSecond : LiveData<Boolean>
        get() = _isSelectReasonSecond

    private val _isSelectReasonThird: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isSelectReasonThird : LiveData<Boolean>
        get() = _isSelectReasonThird

    private val _isSelectReasonFourth: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isSelectReasonFourth : LiveData<Boolean>
        get() = _isSelectReasonFourth

    private val _isSelectReasonFifth: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isSelectReasonFifth : LiveData<Boolean>
        get() = _isSelectReasonFifth

    // 신고 사유 선택 항목
    private val _isSelectReasonType: MutableLiveData<String> = MutableLiveData<String>()
    val isSelectReasonType : LiveData<String>
        get() = _isSelectReasonType

    // 이 사용자의 글 보지 않기 선택 유무
    private val _isHideUserPost: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isHideUserPost : LiveData<Boolean>
        get() = _isHideUserPost

    // 기타 -> 사유 입력 여부
    private val _isCompletedInputReason: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isCompletedInputReason: LiveData<Boolean>
        get() = _isCompletedInputReason

    // 기타 -> 사유
    private val _inputReason: MutableLiveData<String> = MutableLiveData<String>()
    val inputReason: LiveData<String>
        get() = _inputReason

    // 기타 -> 사유 -> 글자수
    private val _currentLength: MutableLiveData<Int> = MutableLiveData<Int>()
    val currentLength: LiveData<Int>
        get() = _currentLength


    fun setUserName(name: String) {
        _userName.value = name
    }

    fun setReasonType(value: String) {
        _isSelectReasonType.value = value
    }

    fun setSelectReasonNumber(type: Int, status: Boolean) {
        _isSelectReasonFirst.value = (type == 1 && status)
        _isSelectReasonSecond.value = (type == 2 && status)
        _isSelectReasonThird.value = (type == 3 && status)
        _isSelectReasonFourth.value = (type == 4 && status)
        _isSelectReasonFifth.value = (type == 5 && status)

        if (type !in 1..5 || !status) {
            _isSelectReasonFirst.value = false
            _isSelectReasonSecond.value = false
            _isSelectReasonThird.value = false
            _isSelectReasonFourth.value = false
            _isSelectReasonFifth.value = false
        }

        _isSelectReasonType.value = when (type) {
            1 -> "판매 금지 물품이에요"
            2 -> "중고거래 게시글이 아니에요"
            3 -> "전문 판매업자 같아요"
            4 -> "사기가 의심돼요"
            5 -> "기타"
            else -> null
        }

        _isSelectReason.value = true
    }


    fun setHideUserPostState(){
        _isHideUserPost.value = _isHideUserPost.value != true
    }

    fun plusHideUser() {
        if(isHideUserPost.value == true) blockedMember()
    }

    fun setCompletedInputReason(status: Boolean) {
        _isCompletedInputReason.value = status
    }

    fun updateTextLength(text: CharSequence?) {
        val length = text?.length ?: 0
        _currentLength.value = length
    }

    // 사용자 차단 기능
    fun blockedMember() = viewModelScope.launch {
        val accessToken = ds.getAccessToken().first()
        try {
            val user = userName.value.toString()
            val blockedMember = Member(name = user)
            val blockDto = BlockDto(blockedMember)
            val response =
                repository.blockUser(accessToken, blockDto)

            response.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        Timber.d("API 호출 성공")
                    } else {
                        val errorBody = response.errorBody()
                        val errorCode = response.code()

                        if (errorBody != null) {
                            val errorJson = JSONObject(errorBody.string())
                            val errorMessage = errorJson.optString("errorMessage")
                            val errorCodeFromJson = errorJson.optInt("code")

                            Timber.d("API 호출 실패: $errorCodeFromJson / $errorMessage")
                        } else Timber.d("API 호출 실패: $errorCode")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Timber.d("RESPONSE FAILURE")
                }
            })
        } catch (e: Exception) {
            "사용자 차단 과정 오류: $e"
        }
    }

    // 글 신고하기 기능
    fun reportUser() = viewModelScope.launch {
        val accessToken = ds.getAccessToken().first()
        val user = userName.value.toString()
        val reason = isSelectReasonType.value.toString()

        try {
            val reportedMember = Member(user)
            val reportedUser = ReportedUser(reportedMember, reason)
            val response =
                repository.reportUser(accessToken, reportedUser)

            response.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        Timber.d("API 호출 성공")
                    } else {
                        val errorBody = response.errorBody()
                        val errorCode = response.code()

                        if (errorBody != null) {
                            val errorJson = JSONObject(errorBody.string())
                            val errorMessage = errorJson.optString("errorMessage")
                            val errorCodeFromJson = errorJson.optInt("code")

                            Timber.d("API 호출 실패: $errorCodeFromJson / $errorMessage")
                        } else Timber.d("API 호출 실패: $errorCode")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Timber.d("RESPONSE FAILURE")
                }
            })
        } catch (e: Exception) {
            "사용자 신고 과정 오류: $e"
        }
    }


    fun initAlLStatus() {
        // 초기 값 설정
        _userName.value = ""
        _isSelectReason.value = false
        _isSelectReasonFirst.value = false
        _isSelectReasonSecond.value = false
        _isSelectReasonThird.value = false
        _isSelectReasonFourth.value = false
        _isSelectReasonFifth.value = false
        _isHideUserPost.value = false
        _isCompletedInputReason.value = false
        _inputReason.value = ""
    }

}