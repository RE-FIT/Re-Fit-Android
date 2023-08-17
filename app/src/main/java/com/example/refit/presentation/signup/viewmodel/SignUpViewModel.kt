package com.example.refit.presentation.signup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.model.common.ResponseError
import com.example.refit.data.model.signup.RegisterUserRequest
import com.example.refit.data.model.signup.RequestEmailCertification
import com.example.refit.data.model.signup.RequestNicknameValidation
import com.example.refit.data.model.signup.ResponseEmailCertification
import com.example.refit.data.repository.signup.SignUpRepository
import com.example.refit.util.Event
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class SignUpViewModel(private val repository: SignUpRepository) : ViewModel() {

    private var _errorResponse = MutableLiveData<Event<ResponseError>>()
    val errorResponse: LiveData<Event<ResponseError>>
        get() = _errorResponse

    private var _errorResponseId = MutableLiveData<Event<ResponseError>>()
    val errorResponseId: LiveData<Event<ResponseError>>
        get() = _errorResponseId

    private var _errorResponseEmail = MutableLiveData<Event<ResponseError>>()
    val errorResponseEmail: LiveData<Event<ResponseError>>
        get() = _errorResponseEmail

    private var _errorResponseNickname = MutableLiveData<Event<ResponseError>>()
    val errorResponseNickname: LiveData<Event<ResponseError>>
        get() = _errorResponseNickname

    // 유효성 상태값

    private var _emailCode = MutableLiveData<Event<ResponseEmailCertification>>()
    val emailCode: LiveData<Event<ResponseEmailCertification>>
        get() = _emailCode

    private var _isValidId = MutableLiveData<Event<Boolean>>()
    val isValidId: LiveData<Event<Boolean>>
        get() = _isValidId

    private var _isValidPassword = MutableLiveData<Event<Boolean>>()
    val isValidPassword: LiveData<Event<Boolean>>
        get() = _isValidPassword

    private var _isValidEmailFormat = MutableLiveData<Event<Boolean>>()
    val isValidEmailFormat: LiveData<Event<Boolean>>
        get() = _isValidEmailFormat

    private var _isValidEmailCodeFormat = MutableLiveData<Event<Boolean>>()
    val isValidEmailCodeFormat: LiveData<Event<Boolean>>
        get() = _isValidEmailCodeFormat

    private var _isRequestEmailCode = MutableLiveData<Event<Boolean>>()
    val isRequestEmailCode: LiveData<Event<Boolean>>
        get() = _isRequestEmailCode

    private var _isValidEmail = MutableLiveData<Event<Boolean>>()
    val isValidEmail: LiveData<Event<Boolean>>
        get() = _isValidEmail

    private var _isValidNicknameFormat = MutableLiveData<Event<Boolean>>()
    val isValidNicknameFormat: LiveData<Event<Boolean>>
        get() = _isValidNicknameFormat

    private var _isValidNickname = MutableLiveData<Event<Boolean>>()
    val isValidNickname: LiveData<Event<Boolean>>
        get() = _isValidNickname

    private var _isValidBirth = MutableLiveData<Event<Boolean>>()
    val isValidBirt: LiveData<Event<Boolean>>
        get() = _isValidBirth

    private var _isValidSex = MutableLiveData<Event<Boolean>>()
    val isValidSex: LiveData<Event<Boolean>>
        get() = _isValidSex

    private var _isValidAgree = MutableLiveData<Event<Boolean>>()
    val isValidAgree: LiveData<Event<Boolean>>
        get() = _isValidAgree

    private var _isSuccessSignUp = MutableLiveData<Event<Boolean>>()
    val isSuccessSignUp: LiveData<Event<Boolean>>
        get() = _isSuccessSignUp


    fun certificateEmail(email: String) {
        viewModelScope.launch {
            try {
                _isRequestEmailCode.value = Event(true)
                val response = repository.requestEmailCertification(RequestEmailCertification(email))
                response.enqueue(object : Callback<ResponseEmailCertification> {
                    override fun onResponse(
                        call: Call<ResponseEmailCertification>,
                        response: Response<ResponseEmailCertification>
                    ) {
                        if (response.isSuccessful) {
                            _emailCode.value = Event(response.body()!!)
                            Timber.d("이메일 인증 코드 : ${response.body()}")
                        } else {
                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            _errorResponseEmail.value = Event(ResponseError(
                                jsonObject.getInt("code"),
                                jsonObject.getString("message")
                            ))
                        }
                        _isRequestEmailCode.value = Event(false)
                    }

                    override fun onFailure(call: Call<ResponseEmailCertification>, t: Throwable) {
                        _isRequestEmailCode.value = Event(false)
                        Timber.d("이메일 인증 코드를 불러오는 데 실패했습니다 - ${_errorResponseEmail.value}")
                    }
                })
            } catch (e: Throwable) {
                Timber.d("이메일 인증 코드 요청에서 오류 발생 : $e")
            }
        }
    }

    fun checkNicknameValidation(nickname: String) {
        viewModelScope.launch {
            try {
                val response =
                    repository.checkNicknameValidation(RequestNicknameValidation(nickname))
                response.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.code() == 200) {
                            _isValidNickname.value = Event(true)
                            Timber.d("사용 가능한 닉네임입니다")
                        } else if (response.code() == 400) {
                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            _isValidNickname.value = Event(false)
                            _errorResponseNickname.value = Event(ResponseError(
                                jsonObject.getInt("code"),
                                jsonObject.getString("message")
                            ))
                            Timber.d("닉네임 사용 불가 - ${_errorResponseNickname.value}")
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Timber.d("닉네임 중복체크 요청 실패2 - $t")
                    }

                })
            } catch (e: Throwable) {
                Timber.d("e")
            }
        }

    }

    fun signUpUser(
        loginId: String, password: String, email: String, name: String, birth: String, gender: Int) {
        viewModelScope.launch {
            try {
                val requestBody = RegisterUserRequest(loginId, password, email, name, birth, gender)
                val response = repository.requestJoinUser(requestBody)
                response.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            _isSuccessSignUp.value = Event(true)
                            Timber.d("회원가입 성공")
                        } else {
                            response.errorBody()?.let {
                                val errorJson = JSONObject(it.string())
                                val message = errorJson.optString("message")
                                val code = errorJson.optInt("code")
                                val responseError = ResponseError(code, message)

                                when (code) {
                                    10013 -> {
                                        _errorResponseId.value = Event(responseError)
                                        _isValidId.value = Event(false)
                                    }
                                }
                            }
                            Timber.d("API 호출 실패: ${response.errorBody()}")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Timber.d("RESPONSE FAILURE")
                    }
                })
            } catch (e: Exception) {
                Timber.d("회원가입 과정 오류 발생: $e")
            }
        }
    }

    fun checkValidationId(regex: Regex, inputText: String) {
        _isValidId.value = Event(regex.matches(inputText))
    }

    fun checkValidationPassword(regex: Regex, inputText: String) {
        _isValidPassword.value = Event(regex.matches(inputText))
    }

    fun checkValidationEmailFormat(regex: Regex, inputText: String) {
        val isValidFormat = regex.matches(inputText)
        _isValidEmailFormat.value = Event(isValidFormat)
    }

    fun checkValidationEmailCodeFormat(regex: Regex, inputText: String) {
        _isValidEmailCodeFormat.value = Event(regex.matches(inputText))
    }

    fun checkValidationEmailCertificationCode(code: String) {
        _emailCode.value?.let {
            _isValidEmail.value = Event(it.content.code == code)
        }
    }

    fun checkValidationNickname(regex: Regex, inputText: String) {
        _isValidNicknameFormat.value = Event(regex.matches(inputText))
    }

    fun checkValidationBirth(regex: Regex, inputText: String) {
        _isValidBirth.value = Event(regex.matches(inputText))
    }

    fun checkValidationSex(isChecked: Boolean) {
        _isValidSex.value = Event(isChecked)
    }

    fun checkValidationAgree(isChecked: Boolean) {
        _isValidAgree.value = Event(isChecked)
    }

    fun handleNoneResponseForEmailCodeRequest(errorMessage: String) {
        if(_isRequestEmailCode.value!!.content) {
            _isRequestEmailCode.value = Event(false)
            _errorResponseEmail.value = Event(ResponseError(
                -1,
                errorMessage
            ))
        }

    }
}