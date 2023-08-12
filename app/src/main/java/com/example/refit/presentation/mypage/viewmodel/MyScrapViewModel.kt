package com.example.refit.presentation.mypage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.model.community.CommunityListItemResponse
import com.example.refit.data.model.mypage.MyScrapGiveListItemResponse
import com.example.refit.data.model.mypage.MyScrapSellListItemResponse
import com.example.refit.data.repository.mypage.MyPageRepository
import com.example.refit.util.Event
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.lang.Exception

class MyScrapViewModel(private val repository: MyPageRepository, private val ds: TokenStore) : ViewModel() {

    // 스크랩 - 판매
    private val _myScrapSellList: MutableLiveData<List<MyScrapSellListItemResponse>> =
        MutableLiveData<List<MyScrapSellListItemResponse>>()
    val myScrapSellList: LiveData<List<MyScrapSellListItemResponse>>
        get() = _myScrapSellList

    // 스크랩 - 나눔
    private val _myScrapGiveList: MutableLiveData<List<MyScrapGiveListItemResponse>> =
        MutableLiveData<List<MyScrapGiveListItemResponse>>()
    val myScrapGiveList: LiveData<List<MyScrapGiveListItemResponse>>
        get() = _myScrapGiveList

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

    enum class Tab2 {
        SELL, GIVE
    }

    private val _selectedTab2 = MutableLiveData<Tab2>()
    val selectedTab2: LiveData<Tab2>
        get() = _selectedTab2

    init {
        _selectedTab2.value = Tab2.SELL // 초기 선택 탭 설정
    }

    fun setSelectedTab(tab: Tab2) {
        _selectedTab2.value = tab
    }

    fun loadScrapSellList() =
        viewModelScope.launch {
            val accessToken = ds.getAccessToken().first()
            _isLoading.value = true
            try {
                val response =
                    repository.loadMyScrapSellList(accessToken)

                response.enqueue(object : Callback<List<MyScrapSellListItemResponse>> {
                    override fun onResponse(
                        call: Call<List<MyScrapSellListItemResponse>>,
                        response: Response<List<MyScrapSellListItemResponse>>
                    ) {
                        if (response.isSuccessful) {
                            Timber.d("API 호출 성공")
                            val responseBody = response.body()
                            if (responseBody != null) {
                                _myScrapSellList.value = response.body() as List<MyScrapSellListItemResponse>
                                Timber.d("scrapSellList : ${response.body()}")
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

                    override fun onFailure(call: Call<List<MyScrapSellListItemResponse>>, t: Throwable) {
                        Timber.d("실패: $t")
                    }
                })
            } catch (e: Exception) {
                "스크랩 글 목록 로딩 오류: $e"
            } finally {
                _isLoading.value = false
            }

        }

    fun loadScrapGiveList() =
        viewModelScope.launch {
            val accessToken = ds.getAccessToken().first()
            _isLoading.value = true
            try {
                val response =
                    repository.loadMyScrapGiveList(accessToken)

                response.enqueue(object : Callback<List<MyScrapGiveListItemResponse>> {
                    override fun onResponse(
                        call: Call<List<MyScrapGiveListItemResponse>>,
                        response: Response<List<MyScrapGiveListItemResponse>>
                    ) {
                        if (response.isSuccessful) {
                            Timber.d("API 호출 성공")
                            val responseBody = response.body()
                            if (responseBody != null) {
                                _myScrapGiveList.value = response.body() as List<MyScrapGiveListItemResponse>
                                Timber.d("scrapList : ${response.body()}")
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

                    override fun onFailure(call: Call<List<MyScrapGiveListItemResponse>>, t: Throwable) {
                        Timber.d("실패: $t")
                    }
                })
            } catch (e: Exception) {
                "스크랩 글 목록 로딩 오류: $e"
            } finally {
                _isLoading.value = false
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

    fun conversionTypeToText(itemType: Int?, value: String?): String {
        return when (itemType) {
            3 -> when (value?.toInt()) {
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

