package com.example.refit.presentation.mypage.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.model.common.ResponseError
import com.example.refit.data.model.mypage.ShowMyInfoResponse
import com.example.refit.data.model.mypage.UpdateDTO
import com.example.refit.data.repository.mypage.MyPageRepository
import com.example.refit.util.Event
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MyInfoViewModel(private val repository: MyPageRepository, private val ds: TokenStore) : ViewModel() {

    // 내 정보
    private val _myInfoResponse: MutableLiveData<ShowMyInfoResponse> =
        MutableLiveData<ShowMyInfoResponse>()
    val myInfoResponse: LiveData<ShowMyInfoResponse>
        get() = _myInfoResponse

    // 변경사항 정보
    private val _change: MutableLiveData<Event<Boolean>> =
        MutableLiveData<Event<Boolean>>()
    val change: LiveData<Event<Boolean>>
        get() = _change

    // 변경되었는지 체크
    private val _isChange: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>()
    val isChange: LiveData<Boolean>
        get() = _isChange

    private var _error = MutableLiveData<Event<ResponseError>>()
    val error : LiveData<Event<ResponseError>>
        get() = _error

    // 변경되었는지 체크
    private val _isSuccess: MutableLiveData<Event<Boolean>> =
        MutableLiveData<Event<Boolean>>()
    val isSuccess: LiveData<Event<Boolean>>
        get() = _isSuccess

    /**
     * 입력값 변경 감지를 위한 LiveData
     * */
    val prevProfileImage: MutableLiveData<String> = MutableLiveData()
    val prevBirth: MutableLiveData<String> = MutableLiveData()
    val prevGender: MutableLiveData<Int> = MutableLiveData()
    val prevName: MutableLiveData<String> = MutableLiveData()
    val fromServer: MutableLiveData<Event<Boolean>> = MutableLiveData()

    val profileImage: MutableLiveData<String> = MutableLiveData()
    val birth: MutableLiveData<String> = MutableLiveData()
    val gender: MutableLiveData<Int> = MutableLiveData()

    /**
     * 초기화
     * */
    fun init(value: ShowMyInfoResponse) {
        prevProfileImage.postValue(value.imageUrl)
        prevName.postValue(value.name)
        prevBirth.postValue(value.birth)
        prevGender.postValue(value.gender)

        profileImage.postValue(value.imageUrl)
        birth.postValue(value.birth)
        gender.postValue(value.gender)

        fromServer.postValue(Event(true))
    }

    fun changed() {
        _change.postValue(Event(true))
    }

    fun isChange(boolean: Boolean) {
        _isChange.postValue(boolean)
    }

    fun setProfileImage(string:String) {
        profileImage.postValue(string)
    }

    fun setBirth(string:String) {
        birth.postValue(string)
    }

    fun setGender(type:Int) {
        gender.postValue(type)
    }

    fun showMyInfoRetrofit() = viewModelScope.launch {
        val token = ds.getAccessToken().first()
        val response = repository.showMyInfo(token)
        response.enqueue(object : Callback<ShowMyInfoResponse> {
            override fun onResponse(call: Call<ShowMyInfoResponse>, response: Response<ShowMyInfoResponse>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _myInfoResponse.postValue(response.body())
                    init(response.body()!!)
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ShowMyInfoResponse>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    fun updateMyInfoRetrofit( image: File?=null
    ) = viewModelScope.launch {
        val token = ds.getAccessToken().first()
        val updateDto = UpdateDTO(
            name = prevName.value.toString(),
            birth = birth.value.toString(),
            gender = gender.value?.toString()!!.toInt(),
            image = profileImage.value.toString()
        )

        var response = repository.updateInfo(token, image, updateDto)

        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    _isSuccess.postValue(Event(true))
                } else {
                    Log.d("RESPONSE", "FAIL")
                    var jsonObject = JSONObject(response.errorBody()!!.string())
                    _error.postValue(Event(ResponseError(
                        jsonObject.getInt("code"), jsonObject.getString("message"))))
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}