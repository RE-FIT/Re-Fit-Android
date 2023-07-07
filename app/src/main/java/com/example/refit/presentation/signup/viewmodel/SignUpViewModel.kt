package com.example.refit.presentation.signup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.model.common.ResponseError
import com.example.refit.data.model.signup.RequestEmailCertification
import com.example.refit.data.model.signup.ResponseEmailCertification
import com.example.refit.data.repository.signup.SignUpRepository
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import timber.log.Timber


class SignUpViewModel(private val repository: SignUpRepository) : ViewModel() {

    private var _errorResponse = MutableLiveData<ResponseError>()
    val errorResponse: LiveData<ResponseError>
        get() = _errorResponse

    private var _emailCode = MutableLiveData<ResponseEmailCertification>()
    val emailCode: LiveData<ResponseEmailCertification>
        get() = _emailCode

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
                            _errorResponse.value = ResponseError(
                                jsonObject.getInt("code"),
                                jsonObject.getString("message")
                            )
                        }
                    }
                    override fun onFailure(call: Call<ResponseEmailCertification>, t: Throwable) {
                        Timber.d("이메일 인증 코드를 불러오는 데 실패했습니다")
                    }
                })
            } catch (e: Throwable) {
                Timber.d("이메일 인증 코드 요청에서 오류 발생 : $e")
            }
        }
    }
}