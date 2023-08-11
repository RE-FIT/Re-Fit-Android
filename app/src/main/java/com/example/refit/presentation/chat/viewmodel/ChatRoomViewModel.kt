package com.example.refit.presentation.chat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.model.chat.ChatRoom
import com.example.refit.data.repository.chat.ChatRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatRoomViewModel(private val repository: ChatRepository, private val ds: TokenStore): ViewModel() {

    private var _rooms = MutableLiveData<List<ChatRoom>>()
    val rooms : LiveData<List<ChatRoom>>
        get() = _rooms

    fun get_rooms() = viewModelScope.launch {

        val accessToken = ds.getAccessToken().first()
        val response = repository.rooms(accessToken)
        response.enqueue(object : Callback<List<ChatRoom>> {
            override fun onResponse(call: Call<List<ChatRoom>>, response: Response<List<ChatRoom>>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _rooms.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<List<ChatRoom>>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}