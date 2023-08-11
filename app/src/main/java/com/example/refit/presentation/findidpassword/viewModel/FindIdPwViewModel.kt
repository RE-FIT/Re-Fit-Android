package com.example.refit.presentation.findidpassword.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.model.common.ResponseError
import com.example.refit.data.model.signup.FindIdRequest
import com.example.refit.data.model.signup.FindIdResponse
import com.example.refit.data.model.signup.FindPasswordRequest
import com.example.refit.data.repository.signup.SignUpRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindIdPwViewModel(private val repository: SignUpRepository): ViewModel() {

    private var _idSuccess = MutableLiveData<Boolean>()
    val idSuccess : LiveData<Boolean>
        get() = _idSuccess

    private var _pwSuccess = MutableLiveData<Boolean>()
    val pwSuccess : LiveData<Boolean>
        get() = _pwSuccess

    private var _error = MutableLiveData<ResponseError>()
    val error : LiveData<ResponseError>
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
                    _idSuccess.postValue(true)
                } else {
                    Log.d("RESPONSE", "FAIL")
                    var jsonObject = JSONObject(response.errorBody()!!.string())
                    _error.postValue(
                        ResponseError(
                        jsonObject.getInt("code"), jsonObject.getString("errorMessage"))
                    )
                }
            }
            override fun onFailure(call: Call<FindIdResponse>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    /**
     * 패스워드 찾기
     */
    fun findByPassword(body : FindPasswordRequest) = viewModelScope.launch {

        val response = repository.findByPassword(body)

        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", "Success")
                    _pwSuccess.postValue(true)
                } else {
                    Log.d("RESPONSE", "FAIL")
                    var jsonObject = JSONObject(response.errorBody()!!.string())
                    _error.postValue(
                        ResponseError(
                            jsonObject.getInt("code"), jsonObject.getString("errorMessage"))
                    )
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}