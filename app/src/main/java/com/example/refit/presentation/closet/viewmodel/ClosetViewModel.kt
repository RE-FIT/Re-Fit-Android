package com.example.refit.presentation.closet.viewmodel

import androidx.datastore.dataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.model.closet.RequestRegisteredClothes
import com.example.refit.data.model.closet.ResponseRegisteredClothes
import com.example.refit.data.repository.colset.ClosetRepository
import com.example.refit.util.Event
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class ClosetViewModel(
    private val repository: ClosetRepository,
    private val dataStore: TokenStore
) : ViewModel() {

    private val _registeredClothes: MutableLiveData<List<ResponseRegisteredClothes>> =
        MutableLiveData<List<ResponseRegisteredClothes>>()
    val registeredClothes: LiveData<List<ResponseRegisteredClothes>>
        get() = _registeredClothes

    private val _selectedRegisteredClothItem: MutableLiveData<Event<ResponseRegisteredClothes>> =
        MutableLiveData<Event<ResponseRegisteredClothes>>()
    val selectedRegisteredClothItem: LiveData<Event<ResponseRegisteredClothes>>
        get() = _selectedRegisteredClothItem

    private val _isSuccessDeleteItem: MutableLiveData<Event<Boolean>> =
        MutableLiveData<Event<Boolean>>()
    val isSuccessDeleteItem: LiveData<Event<Boolean>>
        get() = _isSuccessDeleteItem

    // 옷장 필터링 옵션

    private val _selectedCategoryId: MutableLiveData<Int> = MutableLiveData<Int>(0)
    val selectedCategoryId: LiveData<Int>
        get() = _selectedCategoryId

    private val _selectedSeason: MutableLiveData<String> = MutableLiveData<String>()
    val selectedSeason: LiveData<String>
        get() = _selectedSeason

    private val _selectedSeasonId: MutableLiveData<Int> = MutableLiveData<Int>(0)
    val selectedSeasonId: LiveData<Int>
        get() = _selectedSeasonId

    private val _selectedSortingOption: MutableLiveData<String> = MutableLiveData<String>()
    val selectedSortingOption: LiveData<String>
        get() = _selectedSortingOption

    private val _selectedSortingOptionId: MutableLiveData<String> = MutableLiveData<String>("d-day")
    val selectedSortingOptionId: LiveData<String>
        get() = _selectedSortingOptionId


    fun getUserRegisteredClothes() {
        viewModelScope.launch {
            try {
                val request = RequestRegisteredClothes(
                    _selectedCategoryId.value!!,
                    _selectedSeasonId.value!!,
                    _selectedSortingOptionId.value!!,
                    0,
                    6
                )
                val response = repository.getRegisteredClothes(dataStore.getAccessToken().first(), request)
                response.enqueue(object: Callback<List<ResponseRegisteredClothes>> {
                    override fun onResponse(
                        call: Call<List<ResponseRegisteredClothes>>,
                        response: Response<List<ResponseRegisteredClothes>>
                    ) {
                        if(response.isSuccessful) {
                            _registeredClothes.value = when(response.body()) {
                                null -> listOf()
                                else -> response.body()
                            }

                            Timber.d("등록된 옷 조회 성공 : ${response.body()}")
                        } else {
                            Timber.d("등록된 옷 조회 실패1 : ${response.errorBody()}")
                        }
                    }

                    override fun onFailure(
                        call: Call<List<ResponseRegisteredClothes>>,
                        t: Throwable
                    ) {
                        Timber.d("등록된 옷 조회 실패2 : $t")
                    }

                })
            } catch (e: Throwable) {
                Timber.d("유저가 등록한 옷 정보를 불러오는 데 실패했습니다 : $e")
            }
        }
    }

    fun deleteClothItem(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.deleteClothItem(dataStore.getAccessToken().first(), id)
                response.enqueue(object: Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if(response.code() == 200) {
                            _isSuccessDeleteItem.value = Event(true)
                            getUserRegisteredClothes()
                            Timber.d("옷 아이템 삭제 성공 - id: $id")
                        } else{
                            _isSuccessDeleteItem.value = Event(false)
                            Timber.d("옷 아이템 삭제 실패1 - ${response.errorBody()}")
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        _isSuccessDeleteItem.value = Event(false)
                        Timber.d("옷 아이템 삭제 실패 - $t")
                    }

                })
            } catch (e: Throwable) {
                Timber.d(e)
            }
        }
    }



    fun requestRegisteredItemsByClothCategory(selectedCategoryId: Int) {
        _selectedCategoryId.value = selectedCategoryId
        getUserRegisteredClothes()
        Timber.d("선택된 옷 카테고리 -> ${_selectedCategoryId.value}")
    }

    fun requestSortingBySeason(seasonList: List<String>, selectedSeason: String) {
        _selectedSeason.value = selectedSeason
        _selectedSeasonId.value = seasonList.indexOf(selectedSeason)
        getUserRegisteredClothes()
        Timber.d("계절 옵션에 따른 정렬 요청 -> ${_selectedSeasonId.value} ${_selectedSortingOptionId.value}")
    }

    fun requestSortingByClosetSorting(
        sortingOptionList: List<String>,
        selectedSortingOption: String
    ) {
        _selectedSortingOption.value = selectedSortingOption
        _selectedSortingOptionId.value = when(sortingOptionList.indexOf(selectedSortingOption)) {
            0 -> "d-day"
            1 -> "most-worn"
            else -> "least-worn"
        }
        getUserRegisteredClothes()
        Timber.d("옷장 정렬 옵션에 따른 정렬 요청 -> ${_selectedSeasonId.value} ${_selectedSortingOptionId.value}")
    }

    fun handleClickItem(clothInfo: ResponseRegisteredClothes) {
        _selectedRegisteredClothItem.value = Event(clothInfo)
    }

}