package com.example.refit.presentation.mypage.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.model.common.ResponseError
import com.example.refit.data.model.mypage.MyInfoResponse
import com.example.refit.data.repository.mypage.MyPageRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyViewModel(private val repository: MyPageRepository, private val ds: TokenStore) : ViewModel() {

    // 내 정보
    private val _myInfoResponse: MutableLiveData<MyInfoResponse> =
        MutableLiveData<MyInfoResponse>()
    val myInfoResponse: LiveData<MyInfoResponse>
        get() = _myInfoResponse

    //에러
    private var _error = MutableLiveData<ResponseError>()
    val error : LiveData<ResponseError>
        get() = _error


    fun getMyInfo() = viewModelScope.launch {

        val token = ds.getAccessToken().first()
        val response = repository.myInfo(token)

        response.enqueue(object : Callback<MyInfoResponse> {
            override fun onResponse(call: Call<MyInfoResponse>, response: Response<MyInfoResponse>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", "Success")
                    _myInfoResponse.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                    var jsonObject = JSONObject(response.errorBody()!!.string())
                    _error.postValue(
                        ResponseError(
                        jsonObject.getInt("code"), jsonObject.getString("errorMessage"))
                    )
                }
            }
            override fun onFailure(call: Call<MyInfoResponse>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}