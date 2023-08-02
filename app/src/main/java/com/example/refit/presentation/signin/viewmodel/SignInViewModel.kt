package com.example.refit.presentation.signin.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.model.common.ResponseError
import com.example.refit.data.repository.signup.SignUpRepository
import com.example.refit.data.datastore.TokenStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInViewModel(private val repository: SignUpRepository, private val ds: TokenStore): ViewModel() {

    private var _accessToken = MutableLiveData<String>()
    val accessToken : LiveData<String>
        get() = _accessToken

    private var _logoutSuccess = MutableLiveData<Boolean>()
    val logoutSuccess : LiveData<Boolean>
        get() = _logoutSuccess

    private var _error = MutableLiveData<ResponseError>()
    val error : LiveData<ResponseError>
        get() = _error

    fun setAccessToken(token : String) = viewModelScope.launch {
        ds.setAccessToken(token)
    }

    fun deleteAccessToken() = viewModelScope.launch {
        ds.deleteAccessToken()
    }

    fun basicLogin(loginId: String, password: String) = viewModelScope.launch {
        val response = repository.requestLoginCertification(loginId, password)
        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val accessToken = response.headers().get("Authorization").toString()
                    _accessToken.postValue(accessToken)
                    setAccessToken(accessToken)
                } else {
                    Log.d("RESPONSE", "FAIL")
                    var jsonObject = JSONObject(response.errorBody()!!.string())
                    _error.postValue(ResponseError(
                        jsonObject.getInt("code"), jsonObject.getString("errorMessage")))
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    fun logout() = viewModelScope.launch {

        val token = ds.getAccessToken().first()

        val response = repository.logout(token)
        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    deleteAccessToken()
                    _logoutSuccess.postValue(true)
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

}