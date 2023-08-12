package com.example.refit.presentation.mypage.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.model.mypage.PasswordUpdateRequest
import com.example.refit.data.model.mypage.ShowMyInfoResponse
import com.example.refit.data.repository.mypage.MyPageRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class MyInfoViewModel(private val repository: MyPageRepository, private val ds: TokenStore) : ViewModel() {

    // 내 정보
    private val _myInfoResponse: MutableLiveData<ShowMyInfoResponse> =
        MutableLiveData<ShowMyInfoResponse>()
    val myInfoResponse: LiveData<ShowMyInfoResponse>
        get() = _myInfoResponse

    // 이름(닉네임)
    private val _userNickname: MutableLiveData<String> = MutableLiveData<String>()
    val userNickname: LiveData<String>
        get() = _userNickname

    // 이름(닉네임) - 서버값 여기에 저장 - true 중복 / false 가능
    private val _userNicknameResponse: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val userNicknameResponse: LiveData<Boolean>
        get() = _userNicknameResponse

    // 이름(닉네임) 수정됨?
    private val _isCheckUpdatedNickname: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isCheckUpdatedNickname: LiveData<Boolean>
        get() = _isCheckUpdatedNickname

    // 중복 확인 눌림?
    private val _isCheckUpdatedBtnStatus: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isCheckUpdatedBtnStatus: LiveData<Boolean>
        get() = _isCheckUpdatedBtnStatus

    // 비밀 번호 입력됨?
    private val _isCheckUpdatedPw: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isCheckUpdatedPw: LiveData<Boolean>
        get() = _isCheckUpdatedPw

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

    // 비밀 번호 수정 완료
    private val _isUpdatedPwStatus: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isUpdatedPwStatus: LiveData<Boolean>
        get() = _isUpdatedPwStatus

    // 현재 비밀번호
    private val _currentPassword: MutableLiveData<String> = MutableLiveData<String>()
    val currentPassword: LiveData<String>
        get() = _currentPassword

    // 새로운 비밀번호
    private val _newPassword: MutableLiveData<String> = MutableLiveData<String>()
    val newPassword: LiveData<String>
        get() = _newPassword

    // 입력 항목들 수정 여부
    // 이름/닉네임(0) 생년 월일(1) 성별(2)
    private val _isUpdatedValue: List<MutableLiveData<Boolean>> =
        List(3) { MutableLiveData<Boolean>() }
    val isUpdatedValue: List<LiveData<Boolean>>
        get() = _isUpdatedValue

    // 값이 하나라도 수정이 되었는가?
    private val _isUpdatedOptions: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isUpdatedOptions: LiveData<Boolean>
        get() = _isUpdatedOptions

    // 항목 입력 여부
    // 현재 pw(0) 새로운 pw(1)
    private val _isUpdatedPwValue: List<MutableLiveData<Boolean>> =
        List(2) { MutableLiveData<Boolean>() }
    val isUpdatedPwValue: List<LiveData<Boolean>>
        get() = _isUpdatedPwValue

    private val _isUpdatedPwOptions: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isUpdatedPwOptions: LiveData<Boolean>
        get() = _isUpdatedPwOptions

    // -----------------------------------
    fun setUpdatedStatus(type: Int, status: Boolean) {
        when (type) {
            0 -> _isUpdatedValue[0].value = status// 이름(닉네임)
            1 -> _isUpdatedValue[1].value = status // 생년 월일
            2 -> _isUpdatedValue[2].value = status // 성별
        }
    }

    fun setUpdatedAllStatus() {
        _isUpdatedOptions.value =
            _isUpdatedValue[0].value == true || _isUpdatedValue[1].value == true || _isUpdatedValue[2].value == true

        Log.d("options", "${isUpdatedOptions.value}")
    }

    // 이름(닉네임) 수정 했을 때
    fun updateNickname(newNickname: String) {
        _userNickname.value = newNickname // 새로운 닉네임으로 저장
        _isCheckUpdatedNickname.value = true // 닉네임이 수정되었다는 상태값 저장
    }

    // 이름(닉네임) 중복 확인이 되었다면 true
    fun checkNickname(): Boolean {
        return isCheckUpdatedBtnStatus.value == true && userNicknameResponse.value == false
    }

    // 중복 확인 눌림
    fun updateBtn() {
        _isCheckUpdatedBtnStatus.value = _isCheckUpdatedNickname.value == true
    }

    // 생년 월일 수정
    fun updateBirth(updateBirth: String) {
        _userBirth.value = updateBirth
    }

    // 성별 수정
    fun updateGender(status: Int) {
        _userGender.value = status
    }

    fun updateCurrentPw(nickname: String) {
        _currentPassword.value = nickname
        _isCheckUpdatedPw.value = true
    }
    fun updateNewPw(nickname: String) {
        _isCheckUpdatedPw.value = true
        _newPassword.value = nickname
    }

    fun setUpdatedPasswordStatus(type: Int, status: Boolean) {
        when (type) {
            0 -> {
                _isUpdatedPwValue[0].value = status
            }
            1 -> _isUpdatedPwValue[1].value = status
        }
    }

    fun setUpdatedPasswordAllStatus() {
        _isUpdatedPwOptions.value =
            _isUpdatedPwValue[0].value == true && _isUpdatedPwValue[1].value == true
    }

    fun initNicknameInfoStatus(status: Boolean) {
        _isCheckUpdatedNickname.value = status
    }

    fun initCheckBtnStatus(status: Boolean) {
        _isCheckUpdatedBtnStatus.value = status
    }

    fun initUpdatedPwStatus(status: Boolean) {
        _isUpdatedPwStatus.value = status
    }

    fun initAllStatus() {
        initNicknameInfoStatus(false)
        initCheckBtnStatus(false)
        initUpdatedPwStatus(false)
    }

    // Retrofit
    fun checkNicknameRetrofit() {
        viewModelScope.launch {
            val accessToken = ds.getAccessToken().first()

            try {
                val response =
                    repository.checkNickname("$accessToken", "${_userNickname.value}")
                Log.d("닉네임 값", "${_userNickname.value}")
                Log.d("token", "$accessToken")

                response.enqueue(object : Callback<Boolean> {
                    override fun onResponse(
                        call: Call<Boolean>,
                        response: Response<Boolean>
                    ) {
                        if (response.isSuccessful) {
                            _userNicknameResponse.value = response.body()

                            Timber.d("닉네임 중복 여부: ${_userNicknameResponse.value}")
                        } else {
                            Timber.e("Error: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                        Timber.d("401 Unauthorized: $t")
                    }
                })
            } catch (e: Throwable) {
                Timber.d("ERROR: $e")
            }
        }
    }

    fun showMyInfoRetrofit() {
        viewModelScope.launch {
            val accessToken = ds.getAccessToken().first()

            try {
                val response = repository.showMyInfo("$accessToken")

                response.enqueue(object : Callback<ShowMyInfoResponse> {
                    override fun onResponse(
                        call: Call<ShowMyInfoResponse>,
                        response: Response<ShowMyInfoResponse>
                    ) {
                        if (response.isSuccessful) {
                            _myInfoResponse.value = response.body()
                            Log.d("내 정보 response", "${_myInfoResponse.value}")
                            Log.d("내 정보 2", "${_myInfoResponse.value}")
                        } else {
                            Timber.e("Error: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<ShowMyInfoResponse>, t: Throwable) {
                        Timber.d("401 Unauthorized: $t")
                    }
                })
            } catch (e: Throwable) {
                Timber.d("ERROR: $e")
            }
        }
    }

    fun updatePasswordRetrofit() {
        viewModelScope.launch {
            val accessToken = ds.getAccessToken().first()

            try {
                val request = PasswordUpdateRequest(currentPassword.value, newPassword.value)
                Timber.d("$request")

                val response = repository.updatePassword("$accessToken", request)

                response.enqueue(object : Callback<Response<Void>> {
                    override fun onResponse(
                        call: Call<Response<Void>>,
                        response: Response<Response<Void>>
                    ) {
                        if (response.isSuccessful) {
                            _isCheckUpdatedPw.value = false
                            _isUpdatedPwStatus.value = true

                            Timber.d("비밀번호 수정 API 호출 성공: ${newPassword.value}")
                        } else {
                            _isUpdatedPwStatus.value = false

                            Timber.e("Error: ${response.code()} 비밀번호 일치 X")
                        }
                    }

                    override fun onFailure(call: Call<Response<Void>>, t: Throwable) {
                        Timber.d("401 Unauthorized: $t")
                    }
                })
            } catch (e: Throwable) {
                Timber.d("ERROR: $e")
            }
        }
    }

    fun updateMyInfoRetrofit() {
        viewModelScope.launch {

        }
    }
}