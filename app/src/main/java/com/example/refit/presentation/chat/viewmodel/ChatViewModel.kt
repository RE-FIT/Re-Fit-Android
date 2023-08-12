package com.example.refit.presentation.chat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.model.chat.Chat
import com.example.refit.data.model.chat.CreateRoom
import com.example.refit.data.model.chat.ResponseCreateRoom
import com.example.refit.data.model.common.ResponseError
import com.example.refit.data.repository.chat.ChatRepository
import com.example.refit.util.Event
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class ChatViewModel(private val repository: ChatRepository, private val ds: TokenStore): ViewModel() {

    private var _success = MutableLiveData<Event<Boolean>>()
    val success : LiveData<Event<Boolean>>
        get() = _success

    private var _chats = MutableLiveData<List<Chat>>()
    val chats : LiveData<List<Chat>>
        get() = _chats

    private var _delete = MutableLiveData<Boolean>()
    val delete : LiveData<Boolean>
        get() = _delete

    private var _roomId = MutableLiveData<String>()
    val roomId : LiveData<String>
        get() = _roomId

    private var _userId = MutableLiveData<String>()
    val userId : LiveData<String>
        get() = _userId


    private var _sellerId = MutableLiveData<String>()
    val sellerId : LiveData<String>
        get() = _sellerId

    // 판매자 (0), 구매자 (1)
    private val _userStatus: MutableLiveData<Int> = MutableLiveData<Int>()
    val userStatus : LiveData<Int>
        get() = _userStatus


    fun initDelete() {
        _delete.postValue(false)
    }

    fun initRoomId(data : String) {
        _roomId.postValue(data)
    }


    /**
     * 채팅방 생성
     * */
    fun room_create(room: CreateRoom) = viewModelScope.launch {
        val accessToken = ds.getAccessToken().first()
        val response = repository.create(accessToken, room)
        response.enqueue(object : Callback<ResponseCreateRoom> {
            override fun onResponse(call: Call<ResponseCreateRoom>, response: Response<ResponseCreateRoom>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    initRoomId(response.body()!!.roomId.toString())
                    _success.postValue(Event(true))
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ResponseCreateRoom>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    fun room_detail(roomId: String) = viewModelScope.launch {
        val accessToken = ds.getAccessToken().first()
        val response = repository.chats(accessToken, roomId)
        response.enqueue(object : Callback<List<Chat>> {
            override fun onResponse(call: Call<List<Chat>>, response: Response<List<Chat>>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _chats.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<List<Chat>>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    fun room_delete(roomId: String) = viewModelScope.launch {
        val accessToken = ds.getAccessToken().first()
        val response = repository.delete(accessToken, roomId)
        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    _delete.postValue(true)
                    Log.d("RESPONSE", response.body().toString())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }


    /**
     * 데이터 바인딩
     * */

    fun setUserStatus(userId: String, sellerId: String) {
        _userId.value = userId
        _sellerId.value = sellerId
        if(userId == sellerId) {
            _userStatus.value = 0
        } else _userStatus.value = 1
    }


}