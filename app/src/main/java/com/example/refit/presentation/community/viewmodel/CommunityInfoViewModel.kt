package com.example.refit.presentation.community.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.refit.data.repository.community.CommunityRepository

class CommunityInfoViewModel (
    private val communityRepository: CommunityRepository,
): ViewModel(){

    private val _isScrapState: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isScrapState: LiveData<Boolean>
        get() = _isScrapState

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

    fun classifyUserState(status: Int) {
        when (status) {
            0 -> _UserStatusMainText.value = "수정하기"
            1 -> _UserStatusMainText.value = "수정하기"
            2 -> _UserStatusMainText.value = "수정하기"
            3 -> _UserStatusMainText.value = "이 사용자의 글 보지 않기"
        }

        _UserStatus.value = status
    }

    fun initAllState() {
        _isScrapState.value = false
        _UserStatus.value = 0
        _UserStatusMainText.value = "수정하기"
    }

}