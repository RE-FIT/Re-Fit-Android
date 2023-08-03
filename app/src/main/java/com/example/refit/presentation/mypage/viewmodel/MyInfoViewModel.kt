package com.example.refit.presentation.mypage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.refit.BuildConfig
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.model.mypage.CheckNicknameResponse
import com.example.refit.data.network.api.MyPageApi
import com.example.refit.data.repository.mypage.MyPageRepository
import kotlinx.coroutines.flow.first
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class MyInfoViewModel(private val repository: MyPageRepository, private val ds: TokenStore) : ViewModel() {

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
    private val _userBirth: MutableLiveData<String> = MutableLiveData<String>()
    val userBirth: LiveData<String>
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
        _isCheckNicknameAvailable.value = true
    }

    // 생년 월일 수정
    fun updateBirth(updateBirth: String) {
        _userBirth.value = updateBirth
        onInfoModified(true)
    }

    // 성별 수정
    fun updateGender(status: Int) {
        when (status) {
            0 -> _userGender.value = "남성"
            1 -> _userGender.value = "여성"
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

    private fun initInfoNicknameStatus(status: Boolean) {
        _isCheckNicknameAvailable.value = status
    }

    fun initAllStatus() {
        initInfoModifiedStatus(false)
        initInfoNicknameStatus(false)
    }

    // Retrofit
    suspend fun checkNicknameRetrofit() {
        val token = ds.getAccessToken().first()
        val call = RetrofitBuilder.api.showInfo(token, userNickname)

        call.enqueue(object : Callback<CheckNicknameResponse> {
            override fun onResponse(
                call: Call<CheckNicknameResponse>,
                response: Response<CheckNicknameResponse>
            ) {
                val checkResponse = response.body()
                if (checkResponse != null) {
                    // 성공적인 응답 처리
                    val isDuplicate = checkResponse.checked

                    Timber.d("닉네임 중복 여부: $isDuplicate")
                }
            }

            override fun onFailure(call: Call<CheckNicknameResponse>, t: Throwable) {
                Timber.d("실패")
            }
        })
    }

    object RetrofitBuilder {

        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL) // BuildConfig.BASE_URL이 null일 가능성이 있음
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api: MyPageApi = retrofit.create(MyPageApi::class.java)
    }
}