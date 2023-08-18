package com.example.refit.presentation.mypage.viewmodel

import android.annotation.SuppressLint
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.refit.R
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.model.common.ResponseError
import com.example.refit.data.model.mypage.PasswordUpdateRequest
import com.example.refit.data.model.mypage.ShowMyInfoResponse
import com.example.refit.data.model.mypage.UpdateDTO
import com.example.refit.data.repository.mypage.MyPageRepository
import com.example.refit.presentation.common.DialogUtil.checkPwDialog
import com.example.refit.presentation.common.NavigationUtil.navigateUp
import com.example.refit.presentation.mypage.MyInfoPwUpdateFragment
import com.example.refit.util.Event
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okhttp3.internal.notify
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.File

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
    }

    private var _changeSuccess = MutableLiveData<Event<Boolean>>()
    val changeSuccess : LiveData<Event<Boolean>>
        get() = _changeSuccess

    private var _error = MutableLiveData<Event<ResponseError>>()
    val error : LiveData<Event<ResponseError>>
        get() = _error

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