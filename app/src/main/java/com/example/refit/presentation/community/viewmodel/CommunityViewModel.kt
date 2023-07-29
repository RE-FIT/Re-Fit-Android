package com.example.refit.presentation.community.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.model.community.CommunityListItemResponse
import com.example.refit.data.repository.community.CommunityRepository
import kotlinx.coroutines.Dispatchers
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
): ViewModel() {

    private val _communityList: MutableLiveData<List<CommunityListItemResponse>> =
        MutableLiveData<List<CommunityListItemResponse>>()
    val communityList: LiveData<List<CommunityListItemResponse>>
        get() = _communityList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading



    // 새로운 채팅이 있는가? (for. N 아이콘 표시)
    private val _isNewChat: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isNewChat : LiveData<Boolean>
        get() = _isNewChat


    fun setNewChatIcon(status: Boolean) {
        _isNewChat.value = status
    }

    fun loadCommunityList(accessToken: String, postType: Int, gender: Int, category: Int, region: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response =
                    repository.loadCommunityList(accessToken, postType, gender, category, region)
                response.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            Timber.d("API 호출 성공")
                            // _communityList.value = repository.loadCommunityList(accessToken, postType, gender, category, region)
                        } else {
                            val errorBody = response.errorBody()
                            val errorCode = response.code()

                            if (errorBody != null) {
                                val errorJson = JSONObject(errorBody.string())
                                val errorMessage = errorJson.optString("errorMessage")
                                val errorCodeFromJson = errorJson.optInt("code")

                                Timber.d("API 호출 실패: $errorCodeFromJson")
                            } else Timber.d("API 호출 실패: $errorCode")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Timber.d("RESPONSE FAILURE")
                    }
                })
            } catch (e: Exception) {
                "커뮤니티 글 목록 로딩 오류: $e"
            }

        }
        //val response = communityRepository.loadCommunityList()
    }

    fun getCommunityList() {
        try {
            _communityList.value = listOf(
                /*CommunityListItemResponse(0, "옷 판매합니다", 0, 0, "전국", 6000),
                CommunityListItemResponse(1, "옷 나눔", 1, 0, "전국", 50000),
                CommunityListItemResponse(2, "제목", 0, 1, "서울시 중랑구 묵동", 6000),
                CommunityListItemResponse(3, "옷 판매함", 0, 1, "전국", 0)*/
            )
        } catch (e: Throwable) {
            Timber.d("실패 $e")
        }
    }
}