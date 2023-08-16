package com.example.refit.presentation.findidpassword.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
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

class FindIdViewModel(private val repository: SignUpRepository): ViewModel() {

    private var _idSuccess = MutableLiveData<Event<Boolean>>()
    val idSuccess : LiveData<Event<Boolean>>
        get() = _idSuccess

    private var _id = MutableLiveData<String>()
    val id : LiveData<String>
        get() = _id

    private var _error = MutableLiveData<Event<ResponseError>>()
    val error : LiveData<Event<ResponseError>>
        get() = _error

    /**
     * 아이디 찾기
     */
    fun findById(body : FindIdRequest) = viewModelScope.launch {

        val response = repository.findById(body)

        response.enqueue(object : Callback<FindIdResponse> {
            override fun onResponse(call: Call<FindIdResponse>, response: Response<FindIdResponse>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", "Success")
                    _id.postValue(response.body()!!.loginId)
                    _idSuccess.postValue(Event(true))
                } else {
                    Log.d("RESPONSE", "FAIL")
                    var jsonObject = JSONObject(response.errorBody()!!.string())
                    _error.postValue(Event(
                        ResponseError(
                        jsonObject.getInt("code"), jsonObject.getString("message"))
                    ))
                }
            }
            override fun onFailure(call: Call<FindIdResponse>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}