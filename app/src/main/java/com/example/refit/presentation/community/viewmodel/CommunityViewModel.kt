package com.example.refit.presentation.community.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.refit.data.repository.community.CommunityRepository

class CommunityViewModel(
    private val communityRepository: CommunityRepository,
): ViewModel() {

    // 새로운 채팅이 있는가? (for. N 아이콘 표시)
    private val _isNewChat: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isNewChat : LiveData<Boolean>
        get() = _isNewChat


    fun setNewChatIcon(status: Boolean) {
        _isNewChat.value = status
    }
}