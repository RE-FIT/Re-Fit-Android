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

    fun setScrapState(status: Boolean) {
        _isScrapState.value = status
    }

}