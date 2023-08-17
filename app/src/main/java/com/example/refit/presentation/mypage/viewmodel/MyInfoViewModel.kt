package com.example.refit.presentation.mypage.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.model.common.ResponseError
import com.example.refit.data.model.mypage.ShowMyInfoResponse
import com.example.refit.data.model.mypage.UpdateDTO
import com.example.refit.data.repository.mypage.MyPageRepository
import com.example.refit.util.Event
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MyInfoViewModel(private val repository: MyPageRepository, private val ds: TokenStore) : ViewModel() {

    // 내 정보
    private val _myInfoResponse: MutableLiveData<ShowMyInfoResponse> =
        MutableLiveData<ShowMyInfoResponse>()
    val myInfoResponse: LiveData<ShowMyInfoResponse>
        get() = _myInfoResponse

    // 변경사항 정보
    private val _change: MutableLiveData<Event<Boolean>> =
        MutableLiveData<Event<Boolean>>()
    val change: LiveData<Event<Boolean>>
        get() = _change

    // 변경되었는지 체크
    private val _isChange: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>()
    val isChange: LiveData<Boolean>
        get() = _isChange


    /**
     * 입력값 변경 감지를 위한 LiveData
     * */
    val prevProfileImage: MutableLiveData<String> = MutableLiveData()
    val prevBirth: MutableLiveData<String> = MutableLiveData()
    val prevGender: MutableLiveData<Int> = MutableLiveData()
    val prevName: MutableLiveData<String> = MutableLiveData()
    val fromServer: MutableLiveData<Event<Boolean>> = MutableLiveData()

    val profileImage: MutableLiveData<String> = MutableLiveData()
    val birth: MutableLiveData<String> = MutableLiveData()
    val gender: MutableLiveData<Int> = MutableLiveData()

    /**
     * 초기화
     * */
    fun init(value: ShowMyInfoResponse) {
        prevProfileImage.postValue(value.imageUrl)
        prevName.postValue(value.name)
        prevBirth.postValue(value.birth)
        prevGender.postValue(value.gender)

        profileImage.postValue(value.imageUrl)
        birth.postValue(value.birth)
        gender.postValue(value.gender)

        fromServer.postValue(Event(true))
    }

    fun changed() {
        _change.postValue(Event(true))
    }

    fun isChange(boolean: Boolean) {
        _isChange.postValue(boolean)
    }

    fun setProfileImage(string:String) {
        profileImage.postValue(string)
    }

    fun setBirth(string:String) {
        birth.postValue(string)
    }

    fun setGender(type:Int) {
        gender.postValue(type)
    }

    fun showMyInfoRetrofit() = viewModelScope.launch {
        val token = ds.getAccessToken().first()
        val response = repository.showMyInfo(token)
        response.enqueue(object : Callback<ShowMyInfoResponse> {
            override fun onResponse(call: Call<ShowMyInfoResponse>, response: Response<ShowMyInfoResponse>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _myInfoResponse.postValue(response.body())
                    init(response.body()!!)
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ShowMyInfoResponse>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
//
//    // 이름(닉네임)
//    private val _userNickname: MutableLiveData<String> = MutableLiveData<String>()
//    val userNickname: LiveData<String>
//        get() = _userNickname
//
//    // 이름(닉네임) - 서버값 여기에 저장 - true 중복 / false 가능
//    private val _userNicknameResponse: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
//    val userNicknameResponse: LiveData<Boolean>
//        get() = _userNicknameResponse
//
//    // 이름(닉네임) 수정됨?
//    private val _isCheckUpdatedNickname: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
//    val isCheckUpdatedNickname: LiveData<Boolean>
//        get() = _isCheckUpdatedNickname
//
//    // 중복 확인 눌림?
//    private val _isCheckUpdatedBtnStatus: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
//    val isCheckUpdatedBtnStatus: LiveData<Boolean>
//        get() = _isCheckUpdatedBtnStatus
//
//    // 비밀 번호 입력됨?
//    private val _isCheckUpdatedPw: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
//    val isCheckUpdatedPw: LiveData<Boolean>
//        get() = _isCheckUpdatedPw
//
//    // 이메일
//    private val _userEmail: MutableLiveData<String> = MutableLiveData<String>()
//    val userEmail: LiveData<String>
//        get() = _userEmail
//
//    // 아이디
//    private val _userId: MutableLiveData<String> = MutableLiveData<String>()
//    val userId: LiveData<String>
//        get() = _userId
//
//    // 생년 월일
//    private val _userBirth: MutableLiveData<String> = MutableLiveData<String>()
//    val userBirth: LiveData<String>
//        get() = _userBirth
//
//    // 성별
//    private val _userGender: MutableLiveData<Int> = MutableLiveData<Int>()
//    val userGender: LiveData<Int>
//        get() = _userGender
//
//    // 비밀 번호 수정 완료
//    private val _isUpdatedPwStatus: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
//    val isUpdatedPwStatus: LiveData<Boolean>
//        get() = _isUpdatedPwStatus
//
//    // 현재 비밀번호
//    private val _currentPassword: MutableLiveData<String> = MutableLiveData<String>()
//    val currentPassword: LiveData<String>
//        get() = _currentPassword
//
//    // 새로운 비밀번호
//    private val _newPassword: MutableLiveData<String> = MutableLiveData<String>()
//    val newPassword: LiveData<String>
//        get() = _newPassword
//
//    // 입력 항목들 수정 여부
//    // 이름/닉네임(0) 생년 월일(1) 성별(2)
//    private val _isUpdatedValue: List<MutableLiveData<Boolean>> =
//        List(3) { MutableLiveData<Boolean>() }
//    val isUpdatedValue: List<LiveData<Boolean>>
//        get() = _isUpdatedValue
//
//    // 값이 하나라도 수정이 되었는가?
//    private val _isUpdatedOptions: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
//    val isUpdatedOptions: LiveData<Boolean>
//        get() = _isUpdatedOptions
//
//    // 항목 입력 여부
//    // 현재 pw(0) 새로운 pw(1)
//    private val _isUpdatedPwValue: List<MutableLiveData<Boolean>> =
//        List(2) { MutableLiveData<Boolean>() }
//    val isUpdatedPwValue: List<LiveData<Boolean>>
//        get() = _isUpdatedPwValue
//
//    private val _isUpdatedPwOptions: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
//    val isUpdatedPwOptions: LiveData<Boolean>
//        get() = _isUpdatedPwOptions
//
//
//    // 수정 시에 이미지 변경 여부 확인
//    private val _modifyImageStatus: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
//    val modifyImageStatus: LiveData<Boolean>
//        get() = _modifyImageStatus
//
//    private val _postId: MutableLiveData<Int> = MutableLiveData<Int>()
//    val postId: LiveData<Int>
//        get() = _postId
//
//    private val _selectedPostItem: MutableLiveData<Event<Int>> =
//        MutableLiveData<Event<Int>>()
//    val selectedPostItem: LiveData<Event<Int>>
//        get() = _selectedPostItem
//
//    private val _isModifyPost: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
//    val isModifyPost: LiveData<Boolean>
//        get() = _isModifyPost
//
//    fun setPostId(id: Int) {
//        _postId.value = id
//    }
//
//    // -----------------------------------
//    fun setUpdatedStatus(type: Int, status: Boolean) {
//        when (type) {
//            0 -> _isUpdatedValue[0].value = status// 이름(닉네임)
//            1 -> _isUpdatedValue[1].value = status // 생년 월일
//            2 -> _isUpdatedValue[2].value = status // 성별
//        }
//    }
//
//    fun setUpdatedAllStatus() {
//        _isUpdatedOptions.value =
//            _isUpdatedValue[0].value == true || _isUpdatedValue[1].value == true || _isUpdatedValue[2].value == true
//
//        Log.d("options", "${isUpdatedOptions.value}")
//    }
//
//    // 이름(닉네임) 수정 했을 때
//    fun updateNickname(newNickname: String) {
//        _userNickname.value = newNickname // 새로운 닉네임으로 저장
//        _isCheckUpdatedNickname.value = true // 닉네임이 수정되었다는 상태값 저장
//    }
//
//    // 이름(닉네임) 중복 확인이 되었다면 true
//    fun checkNickname(): Boolean {
//        return isCheckUpdatedBtnStatus.value == true && userNicknameResponse.value == false
//    }
//
//    // 중복 확인 눌림
//    fun updateBtn() {
//        _isCheckUpdatedBtnStatus.value = _isCheckUpdatedNickname.value == true
//    }
//
//    // 생년 월일 수정
//    fun updateBirth(updateBirth: String) {
//        _userBirth.value = updateBirth
//    }
//
//    // 성별 수정
//    fun updateGender(status: Int) {
//        _userGender.value = status
//    }
//
//    fun handleClickItem(postId: Int) {
//        _selectedPostItem.value = Event(postId)
//    }
//
//    fun initNicknameInfoStatus(status: Boolean) {
//        _isCheckUpdatedNickname.value = status
//    }
//
//    fun initCheckBtnStatus(status: Boolean) {
//        _isCheckUpdatedBtnStatus.value = status
//    }
//
//    fun initUpdatedPwStatus(status: Boolean) {
//        _isUpdatedPwStatus.value = status
//    }
//
//    fun initAllStatus() {
//        initNicknameInfoStatus(false)
//        initCheckBtnStatus(false)
//        initUpdatedPwStatus(false)
//        _isUpdatedOptions.value = false
//        _isModifyPost.value = false
//    }
//
//    fun setModifyOrNew(status: Boolean) {
//        _isModifyPost.value = status
//        if(status) {
////            setValueIfModifyStatus()
//        }
//    }
//
//    fun setValueIfModifyStatus() {
///*
//            // 이미지 관련 처리
//            _photoUris.value = postResponse.value?.imgUrls
//            _photoLen.value = postResponse.value?.imgUrls?.size
//            _isFilledValue[0].value = true
//            for (i in 0 until _photoLen.value!!) {
//                _isFilledImageValues[i].value = true*/
//    }


    private var _error = MutableLiveData<Event<ResponseError>>()
    val error : LiveData<Event<ResponseError>>
        get() = _error

    // 변경되었는지 체크
    private val _isSuccess: MutableLiveData<Event<Boolean>> =
        MutableLiveData<Event<Boolean>>()
    val isSuccess: LiveData<Event<Boolean>>
        get() = _isSuccess

    fun updateMyInfoRetrofit( image: File?=null
    ) = viewModelScope.launch {
        val token = ds.getAccessToken().first()
        val updateDto = UpdateDTO(
            name = prevName.value.toString(),
            birth = prevBirth.value.toString(),
            gender = prevGender.value?.toString()!!.toInt()
        )

        var response = repository.updateInfo(token, image, updateDto)

        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    _isSuccess.postValue(Event(true))
                } else {
                    Log.d("RESPONSE", "FAIL")
                    var jsonObject = JSONObject(response.errorBody()!!.string())
                    _error.postValue(Event(ResponseError(
                        jsonObject.getInt("code"), jsonObject.getString("message"))))
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}