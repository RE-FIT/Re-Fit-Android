package com.example.refit.presentation.community.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.repository.community.CommunityRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.lang.Exception

class CommunitySearchViewModel(
    private val repository: CommunityRepository,
    private val ds: TokenStore
): ViewModel() {

    // 검색 타이핑 중인 상태인가? (1)
    private val _isSearchTypingState: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isSearchTypingState: LiveData<Boolean>
        get() = _isSearchTypingState

    // 검색 완료 버튼을 누른 후인가? (1)
    private val _isSearching: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isSearching: LiveData<Boolean>
        get() = _isSearching


    fun setSearchTypingState(status: Boolean) {
        Timber.d("타이핑 중")
        _isSearchTypingState.value = status
    }

    fun setSearchingState(status: Boolean) {
        _isSearching.value = status
        Timber.d("검색 완료 버튼 클릭 : ${isSearching.value}")
    }

    fun loadSearchResult(keyword: String) =
        viewModelScope.launch {
            val accessToken = ds.getAccessToken().first()
            try {
                val response = repository.loadSearchResult(accessToken, keyword)
                response.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            Timber.d("API 호출 성공")
                            val responseBody = response.body()
                            if (responseBody != null) {
                                val json = responseBody.string()

                            }
                        } else {
                            val errorBody = response.errorBody()
                            val errorCode = response.code()

                            if (errorBody != null) {
                                val errorJson = JSONObject(errorBody.string())
                                val errorMessage = errorJson.optString("errorMessage")
                                val errorCodeFromJson = errorJson.optInt("code")

                                Timber.d("API 호출 실패: $errorCodeFromJson / $errorMessage")
                            } else Timber.d("API 호출 실패: $errorCode")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Timber.d("RESPONSE FAILURE")
                    }
                })
            } catch (e: Exception) {
                Timber.d("커뮤니티 검색 과정 오류 발생 : $e")
            }
        }


    fun initStatus() {
        _isSearching.value = false
        _isSearchTypingState.value = false
    }


}