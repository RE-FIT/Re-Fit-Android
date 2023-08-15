package com.example.refit.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.model.common.ResponseError
import com.example.refit.data.repository.signup.SignUpRepository
import com.example.refit.data.datastore.TokenStore
import com.example.refit.util.Event
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccessTokenViewModel(private val repository: SignUpRepository, private val ds: TokenStore): ViewModel() {

    private var _success = MutableLiveData<Event<Boolean>>()
    val success : LiveData<Event<Boolean>>
        get() = _success

    private var _error = MutableLiveData<ResponseError>()
    val error : LiveData<ResponseError>
        get() = _error

    fun checkAccessToken() = viewModelScope.launch {

        val token = ds.getAccessToken().first()

        val response = repository.checkAccessToken(token)

        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", "Success")
                    _success.postValue(Event(true))
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
}