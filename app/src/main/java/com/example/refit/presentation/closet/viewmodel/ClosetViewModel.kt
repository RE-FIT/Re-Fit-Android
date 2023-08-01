package com.example.refit.presentation.closet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.refit.data.model.closet.RegisteredClothInfoResponse
import com.example.refit.data.repository.colset.ClosetRepository
import com.example.refit.util.Event
import timber.log.Timber

class ClosetViewModel(private val repository: ClosetRepository) : ViewModel() {

    private val _registeredClothes: MutableLiveData<List<RegisteredClothInfoResponse>> =
        MutableLiveData<List<RegisteredClothInfoResponse>>()
    val registeredClothes: LiveData<List<RegisteredClothInfoResponse>>
        get() = _registeredClothes

    private val _selectedRegisteredClothItem: MutableLiveData<Event<RegisteredClothInfoResponse>> =
        MutableLiveData<Event<RegisteredClothInfoResponse>>()
    val selectedRegisteredClothItem: LiveData<Event<RegisteredClothInfoResponse>>
        get() = _selectedRegisteredClothItem

    // 옷장 필터링 옵션

    private val _selectedCategory: MutableLiveData<String> = MutableLiveData<String>()
    val selectedCategory: LiveData<String>
        get() = _selectedCategory

    private val _selectedCategoryId: MutableLiveData<Int> = MutableLiveData<Int>()
    val selectedCategoryId: LiveData<Int>
        get() = _selectedCategoryId

    private val _selectedSeason: MutableLiveData<String> = MutableLiveData<String>()
    val selectedSeason: LiveData<String>
        get() = _selectedSeason

    private val _selectedSortingOption: MutableLiveData<String> = MutableLiveData<String>()
    val selectedSortingOption: LiveData<String>
        get() = _selectedSortingOption


    fun getUserRegisteredClothes() {
        try {
            _registeredClothes.value = listOf(
                RegisteredClothInfoResponse(0, 10, 2, 90, 10, 2, 60),
                RegisteredClothInfoResponse(1, 11, 5, 30, 10, 1, 10),
                RegisteredClothInfoResponse(2, 12, 10, 60, 10, 4, 30),
                RegisteredClothInfoResponse(3, 12, 7, 90, 10, 3, 40),
                RegisteredClothInfoResponse(4, 13, 20, 60, 10, 2, 90),
                RegisteredClothInfoResponse(5, 14, 17, 30, 10, 2, 100),
                RegisteredClothInfoResponse(6, 15, 14, 90, 10, 3, 5),
                RegisteredClothInfoResponse(7, 16, 6, 60, 10, 2, 70)
            )
        } catch (e: Throwable) {
            Timber.d("유저가 등록한 옷 정보를 불러오는 데 실패했습니다 : $e")
        }
    }

    fun requestRegisteredItemsByClothCategory(selectedId: Int) {
        _selectedCategoryId.value = selectedId
        Timber.d("선택된 옷 카테고리 -> ${_selectedCategoryId.value}")
    }

    fun requestSortingBySeason(selectedOption: String) {
        _selectedSeason.value = selectedOption
        Timber.d("계절 옵션에 따른 정렬 요청 -> ${_selectedSeason.value} ${_selectedSortingOption.value}")
    }

    fun requestSortingByClosetSorting(selectedOption: String) {
        _selectedSortingOption.value = selectedOption
        Timber.d("옷장 정렬 옵션에 따른 정렬 요청 -> ${_selectedSeason.value} ${_selectedSortingOption.value}")
    }

    fun handleClickItem(clothInfo: RegisteredClothInfoResponse) {
        _selectedRegisteredClothItem.value = Event(clothInfo)
    }

}