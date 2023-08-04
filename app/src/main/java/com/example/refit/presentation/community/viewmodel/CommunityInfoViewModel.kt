package com.example.refit.presentation.community.viewmodel

import android.provider.Settings.Global.getString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.R
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.model.community.PostResponse
import com.example.refit.data.repository.community.CommunityRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.lang.Exception

class CommunityInfoViewModel (
    private val repository: CommunityRepository,
    private val ds: TokenStore
): ViewModel(){

    private val _postId: MutableLiveData<Int> = MutableLiveData<Int>()
    val postId: LiveData<Int>
        get() = _postId

    private val _isScrapState: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isScrapState: LiveData<Boolean>
        get() = _isScrapState

    private val _postResponse: MutableLiveData<PostResponse> = MutableLiveData<PostResponse>()
    val postResponse: LiveData<PostResponse>
        get() = _postResponse

    // 유저 상태
    private val _UserStatus: MutableLiveData<Int> = MutableLiveData<Int>()
    val UserStatus: LiveData<Int>
        get() = _UserStatus

    private val _UserStatusMainText: MutableLiveData<String> = MutableLiveData<String>()
    val UserStatusMainText: LiveData<String>
        get() = _UserStatusMainText


    fun setScrapState(status: Boolean) {
        _isScrapState.value = status
    }

    fun clickedGetPost(postId: Int) {
        _postId.value = postId
        getPost()
    }

    fun conversionType(value: Int): String {
        return when (value) {
            0 -> "나눔 중"
            1 -> "판매 중"
            2 -> "나눔 완료"
            3 -> "판매 완료"
            else -> "UnKnown Data"
        }
    }

    fun classifyUserState(status: Int) {
        when (status) {
            0 -> _UserStatusMainText.value = "수정하기"
            1 -> _UserStatusMainText.value = "수정하기"
            2 -> _UserStatusMainText.value = "수정하기"
            3 -> _UserStatusMainText.value = "이 사용자의 글 보지 않기"
        }

        _UserStatus.value = status
    }

    // 글 조회 기능
    private fun getPost() = viewModelScope.launch {
        val accessToken = ds.getAccessToken().first()
        val postId = _postId.value ?: 0
        try {
            val response =
                repository.getPost(accessToken, postId)

            response.enqueue(object : Callback<PostResponse> {
                override fun onResponse(
                    call: Call<PostResponse>,
                    response: Response<PostResponse>
                ) {
                    if (response.isSuccessful) {
                        Timber.d("API 호출 성공")
                        val postResponse = response.body()
                        val json = postResponse.toString()

                        postResponse?.let {
                            _postResponse.postValue(it)
                        }

                        Timber.d("COMMUNITY POST API 호출 성공 : $json")
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

                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                    Timber.d("RESPONSE FAILURE")
                }
            })
        } catch (e: Exception) {
            "커뮤니티 글 상세 페이지 로딩 오류: $e"
        }
    }

    fun initAllState() {
        _isScrapState.value = false
        _UserStatus.value = 0
        _UserStatusMainText.value = "수정하기"
    }

}