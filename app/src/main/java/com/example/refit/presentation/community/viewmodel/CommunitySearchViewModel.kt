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
import java.lang.Exception

class CommunitySearchViewModel(
    private val repository: CommunityRepository,
    private val ds: TokenStore
): ViewModel() {

    private val _selectedPostItem: MutableLiveData<Event<Int>> =
        MutableLiveData<Event<Int>>()
    val selectedPostItem: LiveData<Event<Int>>
        get() = _selectedPostItem

    private val _searchList: MutableLiveData<List<CommunityListItemResponse>> =
        MutableLiveData<List<CommunityListItemResponse>>()
    val searchList: LiveData<List<CommunityListItemResponse>>
        get() = _searchList

    // 검색 타이핑 중인 상태인가? (1)
    private val _isSearchTypingState: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isSearchTypingState: LiveData<Boolean>
        get() = _isSearchTypingState

    // 검색 완료 버튼을 누른 후인가? (1)
    private val _isSearching: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isSearching: LiveData<Boolean>
        get() = _isSearching

    private val _isVisibleNoResult: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isVisibleNoResult: LiveData<Boolean>
        get() = _isVisibleNoResult

    // response body == null (검색 결과가 없을 때 false)
    private val _isExistResult: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isExistResult: LiveData<Boolean>
        get() = _isExistResult

    // 0 (나눔/판매), 1(여성복/남성복), 2(상의/하의/...)
    private val _dropDownValue: List<MutableLiveData<Int>> = List(3) { MutableLiveData<Int>() }
    val dropDownValue: List<MutableLiveData<Int>>
        get() = _dropDownValue

    private val _searchKeyword: MutableLiveData<String> = MutableLiveData<String>()
    val searchKeyword: LiveData<String>
        get() = _searchKeyword


    fun setSearchTypingState(status: Boolean) {
        Timber.d("타이핑 중")
        _isSearchTypingState.value = status
    }

    fun setSearchingState(status: Boolean) {
        _isSearching.value = status
        Timber.d("검색 완료 버튼 클릭 : ${isSearching.value}")
    }

    fun setKeyword(value: String) {
        _searchKeyword.value = value
    }
    fun setExistResult(status: Boolean) {
        _isExistResult.value = status
    }

    fun loadSearchResult() =
        viewModelScope.launch {
            val accessToken = ds.getAccessToken().first()
            val keyword = _searchKeyword.value ?: ""
            try {

                val postType = _dropDownValue[0].value ?: Integer.MAX_VALUE
                val gender = _dropDownValue[1].value ?: Integer.MAX_VALUE
                val category = _dropDownValue[2].value ?: Integer.MAX_VALUE

                var response = repository.loadSearchResult(accessToken, keyword)

                if (postType < 2) {
                    if (gender > 2 && category > 6)
                        response = repository.loadSearchResulttOnlyPostType(accessToken, keyword, postType)
                    else if (gender < 2 && category > 6)
                        response = repository.loadSearchResultPTAndGender(accessToken, keyword, postType, gender)
                    else if (gender > 2 && category < 6)
                        response = repository.loadSearchResultPTAndCategory(accessToken, keyword, postType, category)
                    else if (gender < 2 && category < 6)
                        response = repository.loadSearchResultAll(accessToken, keyword, postType, gender, category)
                } else {
                    if (gender < 2 && category > 6)
                        response = repository.loadSearchResultOnlyGender(accessToken, keyword, gender)
                    else if (gender > 2 && category < 6)
                        response = repository.loadSearchResultOnlyCategory(accessToken, keyword, category)
                    else if (gender < 2 && category < 6)
                        response = repository.loadSearchResultGenderAndCategory(accessToken, keyword, gender, category)
                }

                response.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            Timber.d("[검색] API 호출 성공")
                            val responseBody = response.body()
                            if (responseBody != null) {
                                val json = responseBody.string()
                                val searchList = parseSearchList(json)
                                Timber.d("searchList : ${searchList.toString()}")

                                _searchList.value = searchList
                                _isExistResult.value = searchList.toString() != "[]"
                            } else {
                                _isExistResult.value = false
                            }
                            noFoundResult()
                            Timber.d("_isExistResult: ${_isExistResult.value}")
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


    fun handleClickItem(postId: Int) {
        _selectedPostItem.value = Event(postId)
    }

    fun initStatus() {
        _isSearching.value = false
        _isSearchTypingState.value = false
        _isExistResult.value = false

        for (item in _dropDownValue) {
            item.value = Integer.MAX_VALUE
        }
    }

    fun noFoundResult() {
        val searching = _isSearching.value ?: false
        val existResult = _isExistResult.value ?: false
        _isVisibleNoResult.value = searching && !existResult
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

    private fun parseSearchList(json: String): List<CommunityListItemResponse> {
        // TODO: JSON 파싱 로직을 구현하여 CommunityListItemResponse 리스트로 반환하는 부분 구현해야 함.
        val gson = Gson()
        return gson.fromJson(json, Array<CommunityListItemResponse>::class.java).toList()
    }

}