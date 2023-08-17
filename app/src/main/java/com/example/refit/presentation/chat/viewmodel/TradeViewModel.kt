package com.example.refit.presentation.chat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.model.chat.Trade
import com.example.refit.data.model.common.ResponseError
import com.example.refit.data.repository.community.CommunityRepository
import com.example.refit.util.Event
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TradeViewModel(private val repository: CommunityRepository, private val ds: TokenStore): ViewModel() {

    private var _error = MutableLiveData<Event<ResponseError>>()
    val error : LiveData<Event<ResponseError>>
        get() = _error

    /**
     * 거래 완료
     * */
    fun trade(req: Trade) = viewModelScope.launch {
        val accessToken = ds.getAccessToken().first()
        val response = repository.trade(accessToken, req)
        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                } else {
                    Log.d("RESPONSE", "FAIL")
                    var jsonObject = JSONObject(response.errorBody()!!.string())
                    _error.postValue(Event(
                        ResponseError(
                            jsonObject.getInt("code"), jsonObject.getString("message"))))
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}