package com.example.refit.presentation.mypage.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.model.common.ResponseError
import com.example.refit.data.model.mypage.PasswordUpdateRequest
import com.example.refit.data.repository.mypage.MyPageRepository
import com.example.refit.util.Event
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PwChangeViewModel(private val repository: MyPageRepository, private val ds: TokenStore) : ViewModel() {


    /**
     * 버튼 활성화 기능
     */
    val pw: MutableLiveData<String> = MutableLiveData()
    val nextPw: MutableLiveData<String> = MutableLiveData()

    val isFilledAllOptions: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(pw) { value = isBothFieldsFilled() }
        addSource(nextPw) { value = isBothFieldsFilled() }
    }

    private fun isBothFieldsFilled(): Boolean {
        return !pw.value.isNullOrEmpty() && !nextPw.value.isNullOrEmpty()
    }

    fun init() {
        pw.postValue("")
        nextPw.postValue("")
        isChange(false)
    }

    private var _changeSuccess = MutableLiveData<Event<Boolean>>()
    val changeSuccess : LiveData<Event<Boolean>>
        get() = _changeSuccess

    private var _error = MutableLiveData<Event<ResponseError>>()
    val error : LiveData<Event<ResponseError>>
        get() = _error

    // 변경되었는지 체크
    private val _isChange: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>()
    val isChange: LiveData<Boolean>
        get() = _isChange

    fun isChange(boolean: Boolean) {
        _isChange.postValue(boolean)
    }

    fun updatePassword(request: PasswordUpdateRequest) = viewModelScope.launch {
        val token = ds.getAccessToken().first()
        val response = repository.updatePassword(token, request)
        response.enqueue(object : Callback<Response<Void>> {
            override fun onResponse(call: Call<Response<Void>>, response: Response<Response<Void>>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _changeSuccess.postValue(Event(true))
                } else {
                    Log.d("RESPONSE", "FAIL")
                    var jsonObject = JSONObject(response.errorBody()!!.string())
                    _error.postValue(Event(ResponseError(
                        jsonObject.getInt("code"), jsonObject.getString("message"))))
                }
            }
            override fun onFailure(call: Call<Response<Void>>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}