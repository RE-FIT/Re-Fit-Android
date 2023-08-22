package com.example.refit.presentation.closet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.model.closet.ForestStamps
import com.example.refit.data.model.closet.ResponseForestStatusInfo
import com.example.refit.data.model.closet.ResponseQuizInfo
import com.example.refit.data.model.closet.ResponseRegisteredClothInfo
import com.example.refit.data.repository.colset.ClosetRepository
import com.example.refit.util.Event
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import kotlin.random.Random

class ForestViewModel(private val repository: ClosetRepository, private val dataStore: TokenStore) :
    ViewModel() {

    private val _forestInfo: MutableLiveData<Event<ResponseForestStatusInfo>> =
        MutableLiveData<Event<ResponseForestStatusInfo>>()
    val forestInfo: LiveData<Event<ResponseForestStatusInfo>>
        get() = _forestInfo

    private val _quizInfo: MutableLiveData<ResponseQuizInfo> =
        MutableLiveData<ResponseQuizInfo>()
    val quizInfo: LiveData<ResponseQuizInfo>
        get() = _quizInfo

    private val _isSelectItem: MutableLiveData<Event<Boolean>> =
        MutableLiveData<Event<Boolean>>()
    val isSelectItem: LiveData<Event<Boolean>>
        get() = _isSelectItem

    private val _forestStamps: MutableLiveData<Event<List<ForestStamps>>> =
        MutableLiveData<Event<List<ForestStamps>>>()
    val forestStamps: LiveData<Event<List<ForestStamps>>>
        get() = _forestStamps

    private val _isValidShowingDialog: MutableLiveData<Event<Boolean>> =
        MutableLiveData<Event<Boolean>>()
    val isValidShowingDialog: LiveData<Event<Boolean>>
        get() = _isValidShowingDialog

    private val _isValidShowingCompletedWindow: MutableLiveData<Event<Boolean>> =
        MutableLiveData<Event<Boolean>>()
    val isValidShowingCompletedWindow: LiveData<Event<Boolean>>
        get() = _isValidShowingCompletedWindow

    private val _clothId: MutableLiveData<Int> =
        MutableLiveData<Int>()
    val clothId: LiveData<Int>
        get() = _clothId

    private val _clothTargetCnt: MutableLiveData<Int> =
        MutableLiveData<Int>()
    val clothTargetCnt: LiveData<Int>
        get() = _clothTargetCnt

    private val _clothItemInfo: MutableLiveData<Event<ResponseRegisteredClothInfo>> =
        MutableLiveData<Event<ResponseRegisteredClothInfo>>()
    val clothItemInfo: LiveData<Event<ResponseRegisteredClothInfo>>
        get() = _clothItemInfo

    // 퀴즈 화면
    private val _isRequestAnswer: MutableLiveData<Event<Boolean>> =
        MutableLiveData<Event<Boolean>>()
    val isRequestAnswer: LiveData<Event<Boolean>>
        get() = _isRequestAnswer

    private val _isRequestExit: MutableLiveData<Event<Boolean>> =
        MutableLiveData<Event<Boolean>>()
    val isRequestExit: LiveData<Event<Boolean>>
        get() = _isRequestExit


    fun getForestInfo() {
        viewModelScope.launch {
            try {
                if (_forestInfo.value == null) {
                    val response = repository.getForestStatusInfo(
                        dataStore.getAccessToken().first(),
                        _clothId.value!!
                    )
                    response.enqueue(object : Callback<ResponseForestStatusInfo> {
                        override fun onResponse(
                            call: Call<ResponseForestStatusInfo>,
                            response: Response<ResponseForestStatusInfo>
                        ) {
                            if (response.isSuccessful) {
                                _forestInfo.value = Event(response.body()!!)
                                _clothTargetCnt.value = response.body()!!.targetCnt
                                _forestStamps.value = Event(getStampList(response.body()!!.targetCnt))
                                _isValidShowingDialog.value = Event(response.body()!!.count < response.body()!!.targetCnt)
                                _isValidShowingCompletedWindow.value = Event(response.body()!!.count == response.body()!!.targetCnt)
                                Timber.d("숲 현황 데이터 불러오기 성공 - ${response.body()}")
                            } else {
                                Timber.d("숲 현황 데이터 불러오기 실패1 - ${response.errorBody()}")
                            }
                        }

                        override fun onFailure(call: Call<ResponseForestStatusInfo>, t: Throwable) {
                            Timber.d("숲 현황 데이터 불러오기 실패2 - $t")
                        }

                    })
                } else {
                    _forestInfo.value = Event(_forestInfo.value!!.content)
                    _forestStamps.value = Event(_forestStamps.value!!.content)
                    stopShowingDialogEver()
                    stopShowingCompletedForestWindow()
                }


            } catch (e: Throwable) {
                Timber.d("숲 현황 데이터 불러오기 실패 : $e")
            }
        }
    }

    fun getRegisteredClothInfo() {
        viewModelScope.launch {
            try {
                val response = repository.getRegisteredClothInfo(
                    dataStore.getAccessToken().first(),
                    _clothId.value!!
                )
                response.enqueue(object : Callback<ResponseRegisteredClothInfo> {
                    override fun onResponse(
                        call: Call<ResponseRegisteredClothInfo>,
                        response: Response<ResponseRegisteredClothInfo>
                    ) {
                        if (response.isSuccessful) {
                            _clothItemInfo.value = Event(response.body()!!)
                            Timber.d("선택된 옷 아이템 정보를 불러오는 데 성공했습니다 - ${response.body()}")
                        } else {
                            Timber.d("선택된 옷 아이템 정보를 불러오는 데 실패했습니다1")
                        }
                    }

                    override fun onFailure(call: Call<ResponseRegisteredClothInfo>, t: Throwable) {
                        Timber.d("선택된 옷 아이템 정보를 불러오는 데 실패했습니다2 - $t")
                    }

                })
            } catch (e: Throwable) {
                Timber.d(e)
            }
        }
    }


    fun handleClickItem(requestedQuizInfoId: Int) {
        _isSelectItem.value = Event(true)
        _quizInfo.value = _forestInfo.value!!.content.questions[requestedQuizInfoId]
    }

    fun checkValidationShowingDialog(isSuccessRequest: Boolean, clothId: Int) {
        _isValidShowingDialog.value = Event(isSuccessRequest)
        _clothId.value = clothId
    }

    fun handleClickQuizButton(isSuccessRequest: Boolean) {
        if (!isSuccessRequest) {
            _isRequestAnswer.value = Event(true)
        } else {
            _isRequestExit.value = Event(true)
            _isRequestAnswer.value = Event(false)
        }
    }

    private fun stopShowingDialogEver() {
        _isValidShowingDialog.value = Event(false)
    }

    private fun stopShowingCompletedForestWindow() {
        _isValidShowingCompletedWindow.value = Event(false)
    }

    private fun getStampList(targetCount: Int): List<ForestStamps> {
        val stampList = mutableListOf<ForestStamps>()
        for (id in 1..targetCount) {
            stampList.add(ForestStamps(id, Random.nextInt(0, 3)))
        }
        return stampList.toList()
    }

}