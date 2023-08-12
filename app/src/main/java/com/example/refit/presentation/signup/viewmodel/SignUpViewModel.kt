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
import retrofit2.Response
import retrofit2.Callback
import timber.log.Timber
import java.lang.Exception


class SignUpViewModel(private val repository: SignUpRepository) : ViewModel() {

    private var _errorResponse = MutableLiveData<ResponseError>()
    val errorResponse: LiveData<ResponseError>
        get() = _errorResponse

    private var _errorResponseId = MutableLiveData<ResponseError>()
    val errorResponseId: LiveData<ResponseError>
        get() = _errorResponseId

    private var _errorResponseEmail = MutableLiveData<ResponseError>()
    val errorResponseEmail: LiveData<ResponseError>
        get() = _errorResponseEmail

    private var _errorResponseNickname = MutableLiveData<ResponseError>()
    val errorResponseNickname: LiveData<ResponseError>
        get() = _errorResponseNickname

    // 유효성 상태값

    private var _emailCode = MutableLiveData<ResponseEmailCertification>()
    val emailCode: LiveData<ResponseEmailCertification>
        get() = _emailCode

    private var _isValidId = MutableLiveData<Boolean>()
    val isValidId: LiveData<Boolean>
        get() = _isValidId

    private var _isValidPassword = MutableLiveData<Boolean>()
    val isValidPassword: LiveData<Boolean>
        get() = _isValidPassword

    private var _isValidEmailFormat = MutableLiveData<Boolean>()
    val isValidEmailFormat: LiveData<Boolean>
        get() = _isValidEmailFormat

    private var _isValidEmailCodeFormat = MutableLiveData<Boolean>()
    val isValidEmailCodeFormat: LiveData<Boolean>
        get() = _isValidEmailCodeFormat

    private var _isValidEmail = MutableLiveData<Boolean>()
    val isValidEmail: LiveData<Boolean>
        get() = _isValidEmail

    private var _isValidNicknameFormat = MutableLiveData<Boolean>()
    val isValidNicknameFormat: LiveData<Boolean>
        get() = _isValidNicknameFormat

    private var _isValidNickname = MutableLiveData<Boolean>()
    val isValidNickname: LiveData<Boolean>
        get() = _isValidNickname

    private var _isValidBirth = MutableLiveData<Boolean>()
    val isValidBirt: LiveData<Boolean>
        get() = _isValidBirth

    private var _isValidSex = MutableLiveData<Boolean>()
    val isValidSex: LiveData<Boolean>
        get() = _isValidSex

    private var _isValidAgree = MutableLiveData<Boolean>()
    val isValidAgree: LiveData<Boolean>
        get() = _isValidAgree

    private var _isSuccessSignUp = MutableLiveData<Event<Boolean>>()
    val isSuccessSignUp: LiveData<Event<Boolean>>
        get() = _isSuccessSignUp



    fun certificateEmail(email: String) {
        viewModelScope.launch {
            try {
                val response =
                    repository.requestEmailCertification(RequestEmailCertification(email))
                response.enqueue(object : Callback<ResponseEmailCertification> {
                    override fun onResponse(
                        call: Call<ResponseEmailCertification>,
                        response: Response<ResponseEmailCertification>
                    ) {
                        if (response.isSuccessful) {
                            _emailCode.value = response.body()
                            Timber.d("이메일 인증 코드 : ${response.body()}")
                        } else {
                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            _errorResponseEmail.value = ResponseError(
                                jsonObject.getInt("code"),
                                jsonObject.getString("message")
                            )
                        }
                    }
                    override fun onFailure(call: Call<ResponseEmailCertification>, t: Throwable) {
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
                val response = repository.checkNicknameValidation(RequestNicknameValidation(nickname))
                response.enqueue(object: Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if(response.code() == 200) {
                            _isValidNickname.value = true
                            Timber.d("사용 가능한 닉네임입니다")
                        } else if(response.code() == 400) {
                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            _isValidNickname.value = false
                            _errorResponseNickname.value = ResponseError(
                                jsonObject.getInt("code"),
                                jsonObject.getString("message")
                            )
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

    fun signUpUser(loginId: String, password: String, email: String, name: String, birth: String, gender: Int) {
        val requestBody = RegisterUserRequest(loginId, password, email, name, birth, gender)

        viewModelScope.launch {
            try {
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
                            val errorBody = response.errorBody()
                            val errorCode = response.code()

                            if (errorBody != null) {
                                val errorJson = JSONObject(errorBody.string())
                                val errorMessage = errorJson.optString("errorMessage")
                                val errorCodeFromJson = errorJson.optInt("code")

                                Timber.d("API 호출 실패: $errorJson")
                            } else Timber.d("API 호출 실패: $errorCode")
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
        _isValidId.value = regex.matches(inputText)
    }

    fun checkValidationPassword(regex: Regex, inputText: String) {
        _isValidPassword.value = regex.matches(inputText)
    }

    fun checkValidationEmailFormat(regex: Regex, inputText: String) {
        _isValidEmailFormat.value = regex.matches(inputText)
    }

    fun checkValidationEmailCodeFormat(regex: Regex, inputText: String) {
        _isValidEmailCodeFormat.value = regex.matches(inputText)
    }

    fun checkValidationEmailCertificationCode(code: String) {
        _emailCode.value?.let {
            _isValidEmail.value = it.code == code
        }
    }

    fun checkValidationNickname(regex: Regex, inputText: String) {
        _isValidNicknameFormat.value = regex.matches(inputText)
    }

    fun checkValidationBirth(regex: Regex, inputText: String) {
        _isValidBirth.value = regex.matches(inputText)
    }

    fun checkValidationSex(isChecked: Boolean) {
        _isValidSex.value = isChecked
    }

    fun checkValidationAgree(isChecked: Boolean) {
        _isValidAgree.value = isChecked
    }


}