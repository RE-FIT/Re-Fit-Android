package com.example.refit.presentation.chat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.model.chat.Chat
import com.example.refit.data.repository.chat.ChatRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatViewModel(private val repository: ChatRepository, private val ds: TokenStore): ViewModel() {

    private var _chats = MutableLiveData<List<Chat>>()
    val chats : LiveData<List<Chat>>
        get() = _chats

    private var _delete = MutableLiveData<Boolean>()
    val delete : LiveData<Boolean>
        get() = _delete

    fun initDelete() {
        _delete.postValue(false)
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
}