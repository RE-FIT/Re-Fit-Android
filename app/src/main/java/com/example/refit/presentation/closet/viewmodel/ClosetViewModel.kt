package com.example.refit.presentation.closet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.refit.data.model.closet.RegisteredClothInfoResponse
import com.example.refit.data.repository.colset.ClosetRepository
import com.example.refit.util.Event
import timber.log.Timber

class ClosetViewModel(private val repository: ClosetRepository): ViewModel() {

    private val _registeredClothes: MutableLiveData<List<RegisteredClothInfoResponse>> = MutableLiveData<List<RegisteredClothInfoResponse>>()
    val registeredClothes: LiveData<List<RegisteredClothInfoResponse>>
        get() = _registeredClothes

    private val _selectedRegisteredClothItem: MutableLiveData<Event<RegisteredClothInfoResponse>> = MutableLiveData<Event<RegisteredClothInfoResponse>>()
    val selectedRegisteredClothItem: LiveData<Event<RegisteredClothInfoResponse>>
        get() = _selectedRegisteredClothItem
    
    fun getUserRegisteredClothes() {
        try {
            _registeredClothes.value = listOf(
                RegisteredClothInfoResponse(0,10, 2, 90, 10, 2, 60),
                RegisteredClothInfoResponse(1,11, 5, 30, 10, 1, 10),
                RegisteredClothInfoResponse(2,12, 10, 60, 10, 4, 30),
                RegisteredClothInfoResponse(3,12, 7, 90, 10, 3, 40),
                RegisteredClothInfoResponse(4,13, 20, 60, 10, 2, 90),
                RegisteredClothInfoResponse(5,14, 17, 30, 10, 2, 100),
                RegisteredClothInfoResponse(6,15, 14, 90, 10, 3, 5),
                RegisteredClothInfoResponse(7,16, 6, 60, 10, 2, 70)
            )
        } catch (e: Throwable) {
            Timber.d("유저가 등록한 옷 정보를 불러오는 데 실패했습니다 : $e")
        }
    }
    
    fun handleClickItem(clothInfo: RegisteredClothInfoResponse) {
        _selectedRegisteredClothItem.value = Event(clothInfo)
    }
}