package com.example.refit.presentation.findidpassword.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.repository.signup.SignUpRepository

class FindIdPasswordViewModel (private val repository: SignUpRepository, private val ds: TokenStore) : ViewModel() {

    // 이름(닉네임)
    private val _userNickname: MutableLiveData<String> = MutableLiveData<String>()
    val userNickname: LiveData<String>
        get() = _userNickname

    // 이메일
    private val _userEmail: MutableLiveData<String> = MutableLiveData<String>()
    val userEmail: LiveData<String>
        get() = _userEmail

    // 아이디
    private val _userId: MutableLiveData<String> = MutableLiveData<String>()
    val userId: LiveData<String>
        get() = _userId

    // 이름(닉네임) 필드
    private val _isFilledUserNickname: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFilledUserNickname: LiveData<Boolean>
        get() = _isFilledUserNickname

    // 이메일 필드
    private val _isFilledUserEmail: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFilledUserEmail: LiveData<Boolean>
        get() = _isFilledUserEmail

    // 아이디 필드
    private val _isFilledUserId: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFilledUserId: LiveData<Boolean>
        get() = _isFilledUserId

    // 필수 입력 항목들 값 채워짐 여부
    // 이름(0) 이메일(1) 아이디(2)
    private val _isFilledValue: List<MutableLiveData<Boolean>> =
        List(3) { MutableLiveData<Boolean>() }
    val isFilledValue: List<LiveData<Boolean>>
        get() = _isFilledValue

    // 등록에 필요한 모든 값이 채워졌는가?
    private val _isFilledAllOptions: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFilledAllOptions: LiveData<Boolean>
        get() = _isFilledAllOptions

/*
    fun editNickname(newName: String) {
        _userNickname.value = newName
        _isFilledValue[0].value = true
    }

    fun editId(newId: String) {
        _userId.value = newId
        _isFilledValue[1].value = true
    }

    fun editEmail(newEmail: String) {
        _userEmail.value = newEmail
        _isFilledValue[2].value = true
    }*/

    fun setFindIdFilledStatus(type: Int, status: Boolean, value: String) {
        when (type) {
            0 -> { // 이름(닉네임)
                _isFilledValue[0].value = status
                _userNickname.value = value
            }
            1 -> { // 이메일
                _isFilledValue[1].value = status
                _userEmail.value = value
            }
        }
    }

    fun setFindPwFilledStatus(type: Int, status: Boolean, value: String) {
        when (type) {
            0 -> { // 이름(닉네임)
                _isFilledValue[0].value = status
                _userNickname.value = value
            }
            1 -> { // 이메일
                _isFilledValue[1].value = status
                _userEmail.value = value
            }
            2 -> { // 아이디
                _isFilledValue[2].value = status
                _userId.value = value
            }
        }
    }

    fun setFindIdAllFilledStatus() {
        _isFilledAllOptions.value =
            isFilledValue[0].value == true && isFilledValue[1].value == true

        Log.d("options", "${isFilledAllOptions.value}")
    }

    fun setFindPwAllFilledStatus() {
        _isFilledAllOptions.value =
            isFilledValue[0].value == true && isFilledValue[1].value == true && isFilledValue[2].value == true

        Log.d("options", "${isFilledAllOptions.value}")
    }

    fun initFilledState() {
        _isFilledAllOptions.value = false

        for (item in _isFilledValue) {
            item.value = false
        }

        _isFilledUserId.value = false
        _isFilledUserEmail.value = false
        _isFilledUserNickname.value = false
    }
}
