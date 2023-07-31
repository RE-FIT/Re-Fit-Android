package com.example.refit.presentation.mypage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.refit.data.repository.mypage.MyPageRepository

class MyInfoViewModel(private val repository: MyPageRepository) : ViewModel() {

    // 값 수정 감지
    private val _isInfoModified: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isInfoModified: LiveData<Boolean>
        get() = _isInfoModified

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

    // 생년 월일
    private val _userBirth: MutableLiveData<Int> = MutableLiveData<Int>()
    val userBirth: LiveData<Int>
        get() = _userBirth

    // 성별
    private val _userGender: MutableLiveData<String> = MutableLiveData<String>()
    val userGender: LiveData<String>
        get() = _userGender

    // 이름(닉네임) 중복 확인
    private val _isCheckNicknameAvailable: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isCheckNicknameAvailable: LiveData<Boolean>
        get() = _isCheckNicknameAvailable

    // 현재 비밀번호, 서버 데이터랑 같은지 확인
    private val _currentPassword: MutableLiveData<String> = MutableLiveData<String>()
    val currentPassword: LiveData<String>
        get() = _currentPassword

    // 새로운 비밀번호
    private val _newPassword: MutableLiveData<String> = MutableLiveData<String>()
    val newPassword: LiveData<String>
        get() = _newPassword

    // -----------------------------------

    // 이름(닉네임) 중복 확인
    fun checkNickname(nickname: String) {

    }

    // 이름(닉네임) 수정
    fun updateNickname(newNickname: String) {
        _userNickname.value = newNickname
        onInfoModified(true)
    }

    // 생년 월일 수정
    fun updateBirth(updateBirth: Int) {
        _userBirth.value = updateBirth
        onInfoModified(true)
    }

    // 성별 수정
    fun updateGender(status: Int) {
        when (status) {
            0 -> _userGender.value = "여자"
            1 -> _userGender.value = "남자"
        }
        onInfoModified(true)
    }

    // 수정된 정보가 있을 때 해당 LiveData들을 true로 업데이트
    private fun onInfoModified(status: Boolean) {
        _isInfoModified.value = status
    }


    // 현재 비밀번호 일치/불일치
    fun checkCurrenPassword(status: Boolean) {

    }

    // 새로운 비밀번호 조건 충족
    fun checkNewPassword(newPw: String) {

    }

    private fun initInfoModifiedStatus(status: Boolean) {
        _isInfoModified.value = status
    }

    fun initAllStatus() {
        initInfoModifiedStatus(false)
    }

}