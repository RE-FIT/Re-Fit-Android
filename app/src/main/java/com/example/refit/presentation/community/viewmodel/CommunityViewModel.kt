package com.example.refit.presentation.community.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.model.closet.RegisteredClothInfoResponse
import com.example.refit.data.model.community.CommunityListItemResponse
import com.example.refit.data.model.community.PostResponse
import com.example.refit.data.repository.community.CommunityRepository
import com.example.refit.util.Event
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.lang.Exception

class CommunityViewModel(
    private val repository: CommunityRepository,
    private val ds: TokenStore
) : ViewModel() {

    private val _communityList: MutableLiveData<List<CommunityListItemResponse>> =
        MutableLiveData<List<CommunityListItemResponse>>()
    val communityList: LiveData<List<CommunityListItemResponse>>
        get() = _communityList

    private val _selectedPostItem: MutableLiveData<Event<Int>> =
        MutableLiveData<Event<Int>>()
    val selectedPostItem: LiveData<Event<Int>>
        get() = _selectedPostItem

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading


    // 새로운 채팅이 있는가? (for. N 아이콘 표시)
    private val _isNewChat: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isNewChat: LiveData<Boolean>
        get() = _isNewChat


    fun setNewChatIcon(status: Boolean) {
        _isNewChat.value = status
    }

    fun loadCommunityList() =
        viewModelScope.launch {
            val accessToken = ds.getAccessToken().first()
            _isLoading.value = true
            try {
                val response =
                    repository.loadCommunityList(accessToken)
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
                                val communityList = parseCommunityList(json)
                                _communityList.value = communityList
                            }
                        } else {
                            val errorBody = response.errorBody()
                            val errorCode = response.code()

                            if (errorBody != null) {
                                val errorJson = JSONObject(errorBody.string())
                                val errorMessage = errorJson.optString("message")
                                val errorCodeFromJson = errorJson.optInt("code")

                                Timber.d("API 호출 실패: ${errorJson.toString()} / $errorCodeFromJson / $errorMessage")
                            } else Timber.d("API 호출 실패 errorbody is not working : $errorCode")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Timber.d("RESPONSE FAILURE")
                    }
                })
            } catch (e: Exception) {
                "커뮤니티 글 목록 로딩 오류: $e"
            } finally {
                _isLoading.value = false
            }

        }

    fun getPost(postId: Int) = viewModelScope.launch {
        val accessToken = ds.getAccessToken().first()
        try {
            val response =
                repository.getPost(accessToken, postId)
            Timber.d("accessToken: ${accessToken.toString()}\n" +
                    "postId: ${postId.toString()}")
            response.enqueue(object : Callback<PostResponse> {
                override fun onResponse(
                    call: Call<PostResponse>,
                    response: Response<PostResponse>
                ) {
                    if (response.isSuccessful) {
                        Timber.d("API 호출 성공")
                        val responseBody = response.body()
                        if (responseBody != null) {
                            val json = responseBody.toString()
                            // val communityList = parseCommunityList(json)
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

                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                    Timber.d("RESPONSE FAILURE")
                }
            })
        } catch (e: Exception) {
            "커뮤니티 글 상세 페이지 로딩 오류: $e"
        }
    }


    fun handleClickItem(postId: Int) {
        _selectedPostItem.value = Event(postId)
    }
}

private fun parseCommunityList(json: String): List<CommunityListItemResponse> {
    // TODO: JSON 파싱 로직을 구현하여 CommunityListItemResponse 리스트로 반환하는 부분 구현해야 함.
    val gson = Gson()
    return gson.fromJson(json, Array<CommunityListItemResponse>::class.java).toList()
}


