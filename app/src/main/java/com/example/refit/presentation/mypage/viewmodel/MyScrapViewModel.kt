package com.example.refit.presentation.mypage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.model.community.CommunityListItemResponse
import com.example.refit.data.model.community.PostResponse
import com.example.refit.data.repository.mypage.MyPageRepository
import com.example.refit.util.Event
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.lang.Exception

class MyScrapViewModel(private val repository: MyPageRepository, private val ds: TokenStore) : ViewModel() {

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

    // 0 (나눔/판매), 1(여성복/남성복), 2(상의/하의/...)
    private val _dropDownValue: List<MutableLiveData<Int>> = List(3) { MutableLiveData<Int>() }
    val dropDownValue: List<MutableLiveData<Int>>
        get() = _dropDownValue

    fun loadCommunityList() =
        viewModelScope.launch {
            val accessToken = ds.getAccessToken().first()
            _isLoading.value = true
            try {
                val postType = _dropDownValue[0].value ?: 0
                val gender = _dropDownValue[1].value ?: 0
                val category = _dropDownValue[2].value ?: 0

                val response =
                    repository.loadCommunityListSort(accessToken, postType, gender, category)

                Timber.d("postType: $postType, gender: $gender, category: $category")
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
                                Timber.d("communityList : ${communityList.toString()}")

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

    fun setDropDownController(type: Int, value: String) {
        when (type) {
            0 -> {
                // 글 타입 (나눔/판매)
                _dropDownValue[0].value = conversionTextToType(0, value)
            }

            1 -> {
                // 성별 (여성복/남성복)
                _dropDownValue[1].value = conversionTextToType(1, value)
            }

            2 -> {
                // 카테고리 (상의/하의/...)
                _dropDownValue[2].value = conversionTextToType(2, value)
            }
        }
    }

    fun handleClickItem(postId: Int) {
        _selectedPostItem.value = Event(postId)
    }

    fun initStatus() {
        for (item in _dropDownValue) {
            item.value = 0
        }
    }

    fun conversionTextToType(itemType: Int, value: String): Int {
        var type = Integer.MIN_VALUE
        when (itemType) {
            0 -> when (value) {
                "나눔" -> type = 0
                "판매" -> type = 1
            }

            1 -> when (value) {
                "여성복" -> type = 0
                "남성복" -> type = 1
            }

            2 -> when (value) {
                "상의" -> type = 0
                "하의" -> type = 1
                "아우터" -> type = 2
                "원피스" -> type = 3
                "신발" -> type = 4
                "악세사리" -> type = 5
            }
        }
        return type
    }

    fun conversionTypeToText(itemType: Int, value: String): String {
        return when (itemType) {
            3 -> when (value.toInt()) {
                0 -> "XS"
                1 -> "S"
                2 -> "M"
                3 -> "L"
                4 -> "XL"
                else -> "Unknown"
            }

            4 -> when (value) {
                "null" -> "전국"
                else -> "Unknown"
            }

            else -> "Unknown"
        }
    }
}

private fun parseCommunityList(json: String): List<CommunityListItemResponse> {
    val gson = Gson()
    return gson.fromJson(json, Array<CommunityListItemResponse>::class.java).toList()
}
