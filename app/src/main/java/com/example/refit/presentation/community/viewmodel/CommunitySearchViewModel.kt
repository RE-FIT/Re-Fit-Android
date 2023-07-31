package com.example.refit.presentation.community.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.refit.data.repository.community.CommunityRepository
import timber.log.Timber

class CommunitySearchViewModel(
    private val communityRepository: CommunityRepository,
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


    fun initStatus() {
        _isSearching.value = false
        _isSearchTypingState.value = false
    }


}