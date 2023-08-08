package com.example.refit.presentation.closet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.model.closet.ForestStamps
import com.example.refit.data.model.closet.ResponseForestStatusInfo
import com.example.refit.data.model.closet.ResponseQuizInfo
import com.example.refit.data.repository.colset.ClosetRepository
import com.example.refit.util.Event
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import kotlin.random.Random

class ForestViewModel(private val repository: ClosetRepository, private val dataStore: TokenStore): ViewModel() {

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

    private val _clothId: MutableLiveData<Int> =
        MutableLiveData<Int>()
    val clothId: LiveData<Int>
        get() = _clothId

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
                val response = repository.getForestStatusInfo(dataStore.getAccessToken().first(), clothId.value!!)
                response.enqueue(object: Callback<ResponseForestStatusInfo> {
                    override fun onResponse(
                        call: Call<ResponseForestStatusInfo>,
                        response: Response<ResponseForestStatusInfo>
                    ) {
                        if(response.isSuccessful) {
                            //TODO(카카오톡 로그인이 추가되면 옷장이 완성됐을 때 카톡 공유 기능도 추가할 것)
                            _forestInfo.value = Event(response.body()!!)
                            val stampList = mutableListOf<ForestStamps>()
                            for(id in 1 .. response.body()!!.targetCnt) {
                                stampList.add(ForestStamps(id, Random.nextInt(0, 3)))
                            }
                            _forestStamps.value = Event(stampList.toList())
//                            _isValidShowingDialog.value = Event(false)
                            Timber.d("숲 현황 데이터 불러오기 성공 - ${response.body()}")
                        } else {
                            Timber.d("숲 현황 데이터 불러오기 실패1 - ${response.errorBody()}")
                        }
                    }

                    override fun onFailure(call: Call<ResponseForestStatusInfo>, t: Throwable) {
                        Timber.d("숲 현황 데이터 불러오기 실패2 - $t")
                    }

                })

            } catch (e: Throwable) {
                Timber.d("숲 현황 데이터 불러오기 실패 : $e")
            }
        }
    }

    fun getQuiz() {
        viewModelScope.launch {
            try {
                val response = repository.getQuizInfo(dataStore.getAccessToken().first(), clothId.value!!)
                response.enqueue(object: Callback<ResponseQuizInfo> {
                    override fun onResponse(
                        call: Call<ResponseQuizInfo>,
                        response: Response<ResponseQuizInfo>
                    ) {
                        if(response.isSuccessful) {
                            _quizInfo.value = response.body()
                            Timber.d("퀴즈 데이터 불러오기 성공 - ${response.body()}")
                        } else {
                            Timber.d("퀴즈 데이터 불러오기 실패1 - ${response.errorBody()}")
                        }
                    }

                    override fun onFailure(call: Call<ResponseQuizInfo>, t: Throwable) {
                        Timber.d("퀴즈 데이터 불러오기 실패2 - $t")
                    }

                })
            } catch (e: Throwable) {
                Timber.d(e)
            }
        }
    }


    fun handleClickItem() {
        _isSelectItem.value = Event(true)
    }
    
    fun checkValidationShowingDialog(isSuccessRequest: Boolean, clothId: Int) {
        _isValidShowingDialog.value = Event(isSuccessRequest)
        _clothId.value = clothId
    }

    fun handleClickQuizButton(isSuccessRequest: Boolean) {
        if(!isSuccessRequest) {
            _isRequestAnswer.value = Event(true)
        } else {
            _isRequestExit.value = Event(true)
            _isRequestAnswer.value = Event(false)
        }
    }

    fun stopShowingDialogEver() {
        _isValidShowingDialog.value = Event(false)
    }

}