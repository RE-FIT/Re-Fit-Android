package com.example.refit.presentation.mypage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.model.mypage.CheckNicknameResponse
import com.example.refit.data.repository.mypage.MyPageRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class MyInfoViewModel(private val repository: MyPageRepository, private val ds: TokenStore) : ViewModel() {

    // 이름(닉네임)
    private val _userNickname: MutableLiveData<String> = MutableLiveData<String>()
    val userNickname: LiveData<String>
        get() = _userNickname

    // 이름(닉네임) 수정됨?
    private val _isCheckUpdatedNickname: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isCheckUpdatedNickname: LiveData<Boolean>
        get() = _isCheckUpdatedNickname

    // 생일 수정됨?
    private val _isCheckUpdatedBirth: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isCheckUpdatedBirth: LiveData<Boolean>
        get() = _isCheckUpdatedBirth

    // 성별 수정됨?
    private val _isCheckUpdatedGender: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isCheckUpdatedGender: LiveData<Boolean>
        get() = _isCheckUpdatedGender

    // 비밀 번호 입력됨?
    private val _isCheckUpdatedPw: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isCheckUpdatedPw: LiveData<Boolean>
        get() = _isCheckUpdatedPw

    private val _checkNickname: MutableLiveData<CheckNicknameResponse> = MutableLiveData<CheckNicknameResponse>()
    val checkNickname: LiveData<CheckNicknameResponse>
        get() = _checkNickname

    // 이메일
    private val _userEmail: MutableLiveData<String> = MutableLiveData<String>()
    val userEmail: LiveData<String>
        get() = _userEmail

    // 아이디
    private val _userId: MutableLiveData<String> = MutableLiveData<String>()
    val userId: LiveData<String>
        get() = _userId

    // 생년 월일
    private val _userBirth: MutableLiveData<String> = MutableLiveData<String>()
    val userBirth: LiveData<String>
        get() = _userBirth

    // 성별
    private val _userGender: MutableLiveData<Int> = MutableLiveData<Int>()
    val userGender: LiveData<Int>
        get() = _userGender

    // 입력한 현재 비밀 번호
    private val _editCurrentPassword: MutableLiveData<String> = MutableLiveData<String>()
    val editCurrentPassword: LiveData<String>
        get() = _editCurrentPassword

    //  서버에 있는 현재 비밀 번호
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
        _isCheckUpdatedNickname.value = true
    }

    // 생년 월일 수정
    fun updateBirth(updateBirth: String) {
        _userBirth.value = updateBirth
        _isCheckUpdatedBirth.value = true
    }

    // 성별 수정
    fun updateGender(status: Int) {
        _userGender.value = status
        _isCheckUpdatedGender.value = true
    }

    // 새로운 비밀 번호 수정
    fun updateCurrentPw(currentPw: String) {
        _newPassword.value = currentPw
        _isCheckUpdatedPw.value = true
    }

    private fun initNicknameInfoStatus(status: Boolean) {
        _isCheckUpdatedNickname.value = status
    }

    private fun initBirthInfoStatus(status: Boolean) {
        _isCheckUpdatedBirth.value = status
    }

    private fun initGenderInfoStatus(status: Boolean) {
        _isCheckUpdatedGender.value = status
    }

    fun initAllStatus() {
        initNicknameInfoStatus(false)
        initBirthInfoStatus(false)
        initGenderInfoStatus(false)
    }

    // Retrofit
    fun checkNicknameRetrofit() {
        viewModelScope.launch {
            val token = ds.getAccessToken().first()

            try {
                val response = repository.checkNickname("$token", "${_userNickname.value}")

                response.enqueue(object : Callback<CheckNicknameResponse> {
                    override fun onResponse(
                        call: Call<CheckNicknameResponse>,
                        response: Response<CheckNicknameResponse>
                    ) {
                        if (response.isSuccessful) {
                            _checkNickname.value = response.body()
                            Timber.d("닉네임 중복 여부: ${response.body()}")
                        } else {
                            Timber.d("404 Not Found")
                        }
                    }
                    override fun onFailure(call: Call<CheckNicknameResponse>, t: Throwable) {
                        Timber.d("401 Unauthorized: $t")
                    }
                })
            } catch (e: Throwable) {
                Timber.d("ERROR: $e")
            }
        }
    }
}