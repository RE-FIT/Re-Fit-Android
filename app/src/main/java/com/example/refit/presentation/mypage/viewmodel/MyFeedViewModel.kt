package com.example.refit.presentation.mypage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.refit.data.model.community.CommunityListItemResponse
import com.example.refit.data.repository.mypage.MyPageRepository
import timber.log.Timber

class MyFeedViewModel(private val repository: MyPageRepository) : ViewModel() {

    private val _feedList: MutableLiveData<List<CommunityListItemResponse>> = MutableLiveData<List<CommunityListItemResponse>>()
    val feedList: LiveData<List<CommunityListItemResponse>>
        get() = _feedList

    fun getCommunityList() {
        try {
            _feedList.value = listOf(
                CommunityListItemResponse(0, "옷 판매합니다", 0, 0, "전국", 6000),
                CommunityListItemResponse(1, "옷 나눔", 1, 0, "전국", 50000),
                CommunityListItemResponse(2, "제목", 0, 1, "서울시 중랑구 묵동", 6000),
                CommunityListItemResponse(3, "옷 판매함", 0, 1, "전국", 0)
            )
        } catch (e: Throwable) {
            Timber.d("실패 $e")
        }
    }
}