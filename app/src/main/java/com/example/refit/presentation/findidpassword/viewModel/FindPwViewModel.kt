package com.example.refit.presentation.findidpassword.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.model.common.ResponseError
import com.example.refit.data.model.signup.FindIdRequest
import com.example.refit.data.model.signup.FindIdResponse
import com.example.refit.data.model.signup.FindPasswordRequest
import com.example.refit.data.repository.signup.SignUpRepository
import com.example.refit.util.Event
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindPwViewModel(private val repository: SignUpRepository): ViewModel() {

    private var _pwSuccess = MutableLiveData<Event<Boolean>>()
    val pwSuccess : LiveData<Event<Boolean>>
        get() = _pwSuccess

    private var _error = MutableLiveData<Event<ResponseError>>()
    val error : LiveData<Event<ResponseError>>
        get() = _error

    /**
     * 패스워드 찾기
     */
    fun findByPassword(body : FindPasswordRequest) = viewModelScope.launch {

        val response = repository.findByPassword(body)

        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", "Success")
                    _pwSuccess.postValue(Event(true))
                } else {
                    Log.d("RESPONSE", "FAIL")
                    var jsonObject = JSONObject(response.errorBody()!!.string())
                    _error.postValue(
                        Event(ResponseError(
                            jsonObject.getInt("code"), jsonObject.getString("message"))
                    ))
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    // EditText의 값들을 추적
    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val loginId = MutableLiveData<String>()

    // 두 EditText가 모두 채워져 있을 때 true를 반환하는 LiveData
    val isFilledAllOptions: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(name) { value = isBothFieldsFilled() }
        addSource(email) { value = isBothFieldsFilled() }
        addSource(loginId) { value = isBothFieldsFilled() }
    }

    private fun isBothFieldsFilled(): Boolean {
        return !name.value.isNullOrEmpty() && !email.value.isNullOrEmpty() && !loginId.value.isNullOrEmpty()
    }

    fun init() {
        name.postValue("")
        email.postValue("")
        loginId.postValue("")
    }
}