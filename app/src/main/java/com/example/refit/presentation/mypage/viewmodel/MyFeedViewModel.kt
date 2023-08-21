package com.example.refit.presentation.mypage.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.model.community.CommunityListItemResponse
import com.example.refit.data.model.mypage.MyFeedBuyListItemResponse
import com.example.refit.data.model.mypage.MyFeedGiveListItemResponse
import com.example.refit.data.model.mypage.MyFeedSellListItemResponse
import com.example.refit.data.model.mypage.MyScrapGiveListItemResponse
import com.example.refit.data.model.mypage.MyScrapSellListItemResponse
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

class MyFeedViewModel(private val repository: MyPageRepository, private val ds: TokenStore) : ViewModel() {

    private val _successGive: MutableLiveData<Event<Boolean>> =
        MutableLiveData<Event<Boolean>>()
    val successGive: LiveData<Event<Boolean>>
        get() = _successGive

    private val _successSell: MutableLiveData<Event<Boolean>> =
        MutableLiveData<Event<Boolean>>()
    val successSell: LiveData<Event<Boolean>>
        get() = _successSell

    private val _successBuy: MutableLiveData<Event<Boolean>> =
        MutableLiveData<Event<Boolean>>()
    val successBuy: LiveData<Event<Boolean>>
        get() = _successBuy

    private val _init: MutableLiveData<Event<Boolean>> =
        MutableLiveData<Event<Boolean>>()
    val init: LiveData<Event<Boolean>>
        get() = _init

    private val _postId: MutableLiveData<Int> = MutableLiveData<Int>()
    val postId: LiveData<Int>
        get() = _postId

    // 피드 - 판매
    private val _myFeedSellList: MutableLiveData<List<MyFeedSellListItemResponse>> =
        MutableLiveData<List<MyFeedSellListItemResponse>>()
    val myFeedSellList: LiveData<List<MyFeedSellListItemResponse>>
        get() = _myFeedSellList

    // 피드 - 나눔
    private val _myFeedGiveList: MutableLiveData<List<MyFeedGiveListItemResponse>> =
        MutableLiveData<List<MyFeedGiveListItemResponse>>()
    val myFeedGiveList: LiveData<List<MyFeedGiveListItemResponse>>
        get() = _myFeedGiveList

    // 피드 - 구매
    private val _myFeedBuyList: MutableLiveData<List<MyFeedBuyListItemResponse>> =
        MutableLiveData<List<MyFeedBuyListItemResponse>>()
    val myFeedBuyList: LiveData<List<MyFeedBuyListItemResponse>>
        get() = _myFeedBuyList

    private val _selectedPostItem: MutableLiveData<Event<Int>> =
        MutableLiveData<Event<Int>>()

    val selectedPostItem: LiveData<Event<Int>>
    get() = _selectedPostItem

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
    get() = _isLoading

    enum class Tab {
        SELL, GIVE, BUY
    }

    private val _selectedTab = MutableLiveData<Tab>()
    val selectedTab: LiveData<Tab>
        get() = _selectedTab

    fun setSelectedTab(tab: Tab) {
        _selectedTab.value = tab
    }

    fun loadFeedGiveList() =
        viewModelScope.launch {
            val accessToken = ds.getAccessToken().first()
            _isLoading.value = true
            try {
                val response =
                    repository.loadMyFeedGiveList(accessToken)

                response.enqueue(object : Callback<List<MyFeedGiveListItemResponse>> {
                    override fun onResponse(
                        call: Call<List<MyFeedGiveListItemResponse>>,
                        response: Response<List<MyFeedGiveListItemResponse>>
                    ) {
                        if (response.isSuccessful) {
                            Timber.d("API 호출 성공")
                            val responseBody = response.body()
                            if (responseBody != null) {
                                _myFeedGiveList.value = responseBody ?: null
                                Timber.d("scrapList : ${response.body()}")
                            }
                            _successGive.postValue(Event(true))
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

                    override fun onFailure(call: Call<List<MyFeedGiveListItemResponse>>, t: Throwable) {
                        Timber.d("실패: $t")
                    }
                })
            } catch (e: Exception) {
                "내 피드 나눔 글 목록 로딩 오류: $e"
            } finally {
                _isLoading.value = false
            }

        }

    fun loadFeedSellList() =
        viewModelScope.launch {
            val accessToken = ds.getAccessToken().first()
            _isLoading.value = true
            try {
                val response =
                    repository.loadMyFeedSellList(accessToken)

                response.enqueue(object : Callback<List<MyFeedSellListItemResponse>> {
                    override fun onResponse(
                        call: Call<List<MyFeedSellListItemResponse>>,
                        response: Response<List<MyFeedSellListItemResponse>>
                    ) {
                        if (response.isSuccessful) {
                            Timber.d("API 호출 성공")
                            val responseBody = response.body()
                            if (responseBody != null) {
                                _myFeedSellList.value = responseBody ?: null
                                Timber.d("feedSellList : ${response.body()}")
                            }
                            _successSell.postValue(Event(true))
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

                    override fun onFailure(call: Call<List<MyFeedSellListItemResponse>>, t: Throwable) {
                        Timber.d("실패: $t")
                    }
                })
            } catch (e: Exception) {
                "내 피드 판매 글 목록 로딩 오류: $e"
            } finally {
                _isLoading.value = false
            }

        }

    fun loadFeedBuyList() =
        viewModelScope.launch {
            val accessToken = ds.getAccessToken().first()
            _isLoading.value = true
            try {
                val response =
                    repository.loadMyFeedBuyList(accessToken)

                response.enqueue(object : Callback<List<MyFeedBuyListItemResponse>> {
                    override fun onResponse(
                        call: Call<List<MyFeedBuyListItemResponse>>,
                        response: Response<List<MyFeedBuyListItemResponse>>
                    ) {
                        if (response.isSuccessful) {
                            Timber.d("API 호출 성공")
                            val responseBody = response.body()
                            if (responseBody != null) {
                                _myFeedBuyList.value = responseBody ?: null
                                Timber.d("scrapList : ${response.body()}")
                            }
                            _successBuy.postValue(Event(true))
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

                    override fun onFailure(call: Call<List<MyFeedBuyListItemResponse>>, t: Throwable) {
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



