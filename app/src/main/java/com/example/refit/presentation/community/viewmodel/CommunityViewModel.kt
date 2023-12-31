package com.example.refit.presentation.community.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.model.community.CommunityListItemResponse
import com.example.refit.data.repository.community.CommunityRepository
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

class CommunityViewModel(
    private val repository: CommunityRepository,
    private val ds: TokenStore
) : ViewModel() {

    private val _scrollStatus: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val scrollStatus: LiveData<Boolean>
        get() = _scrollStatus

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

    // 새로운 채팅이 있는가? (for. N 아이콘 표시)
    private val _isNewChat: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isNewChat: LiveData<Boolean>
        get() = _isNewChat

    fun setScrollStatus(boolean: Boolean) {
        _scrollStatus.value = boolean
    }

    fun initCommunityList() = viewModelScope.launch {
        val accessToken = ds.getAccessToken().first()
        _isLoading.value = true
        try {
            val response =
                repository.initCommunityList(accessToken)

            response.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        Timber.d("init API 호출 성공")
                        val responseBody = response.body()
                        if (responseBody != null) {
                            val json = responseBody.string()
                            val communitylist = parseCommunityList(json)
                            Timber.d("communitylist 초기 : ${communitylist.toString()}")
                            _communityList.value = communitylist
                        }
                    } else {
                        val errorBody = response.errorBody()
                        val errorCode = response.code()

                        if (errorBody != null) {
                            val errorJson = JSONObject(errorBody.string())

                            Timber.d("API 호출 실패: ${errorJson.toString()}")
                        } else Timber.d("API 호출 실패 errorbody is not working : $errorCode")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Timber.d("RESPONSE FAILURE")
                }
            })
        } catch (e: Exception) {
            "커뮤니티 글 목록 초기 로딩 오류: $e"
        } finally {

        }

    }

    fun loadCommunityList() =
        viewModelScope.launch {
            val accessToken = ds.getAccessToken().first()
            _isLoading.value = true
            try {
                val postType = _dropDownValue[0].value ?: Integer.MAX_VALUE
                val gender = _dropDownValue[1].value ?: Integer.MAX_VALUE
                val category = _dropDownValue[2].value ?: Integer.MAX_VALUE

                var response =
                    repository.loadCommuintyListSort(accessToken, postType, gender, category)

                if (postType < 2) {
                    if (gender > 2 && category > 6)
                        response = repository.loadCommunityOnlyPostType(accessToken, postType)
                    else if (gender < 2 && category > 6)
                        response = repository.loadCommunityPTAndGender(accessToken, postType, gender)
                    else if (gender > 2 && category < 6)
                        response = repository.loadCommunityPTAndCategory(accessToken, postType, category)
                } else {
                    if (gender < 2 && category > 6)
                        response = repository.loadCommunityOnlyGender(accessToken, gender)
                    else if (gender > 2 && category < 6)
                        response = repository.loadCommunityOnlyCategory(accessToken, category)
                    else if (gender < 2 && category < 6)
                        response = repository.loadCommunityGenderAndCategory(accessToken, gender, category)
                }

                Timber.d("[커뮤니티 드롭다운 테스트] postType: $postType, gender: $gender, category: $category")
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
                                val communitylist = parseCommunityList(json)
                                Timber.d("communitylist : ${communitylist.toString()}")

                                _communityList.value = communitylist
                            }
                        } else {
                            val errorBody = response.errorBody()
                            val errorCode = response.code()

                            if (errorBody != null) {
                                val errorJson = JSONObject(errorBody.string())
                                Timber.d("API 호출 실패: ${errorJson.toString()}")
                            }
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
            item.value = Integer.MAX_VALUE
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
}


private fun parseCommunityList(json: String): List<CommunityListItemResponse> {
    // TODO: JSON 파싱 로직을 구현하여 CommunityListItemResponse 리스트로 반환하는 부분 구현해야 함.
    val gson = Gson()
    return gson.fromJson(json, Array<CommunityListItemResponse>::class.java).toList()
}
