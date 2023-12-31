package com.example.refit.presentation.closet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.refit.data.datastore.TokenStore
import com.example.refit.data.model.closet.RequestAddNewCloth
import com.example.refit.data.model.closet.RequestResetCompletedCloth
import com.example.refit.data.model.closet.ResponseRegisteredClothInfo
import com.example.refit.data.repository.colset.ClosetRepository
import com.example.refit.util.Event
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.File
import java.util.Calendar

class ClothAddViewModel(
    private val repository: ClosetRepository,
    private val dataStore: TokenStore
) : ViewModel() {

    // 항목별 컨테이너 상태 값

    private val _isValidInvalidSeasonConfirm: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isValidInvalidSeasonConfirm: LiveData<Boolean>
        get() = _isValidInvalidSeasonConfirm

    private val _isValidShowingWearingGoal: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isValidShowingWearingGoal: LiveData<Boolean>
        get() = _isValidShowingWearingGoal

    private val _isNegativeInvalidSeasonConfirm: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>()
    val isNegativeInvalidSeasonConfirm: LiveData<Boolean>
        get() = _isNegativeInvalidSeasonConfirm

    private val _isValidShowingRecommendWearing: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>()
    val isValidShowingRecommendWearing: LiveData<Boolean>
        get() = _isValidShowingRecommendWearing

    // 컨테이너 내 뷰 상태 값

    private val _isFocusMonthPopup: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFocusMonthPopup: LiveData<Boolean>
        get() = _isFocusMonthPopup

    private val _isFocusWearingNumberPopup: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isFocusWearingNumberPopup: LiveData<Boolean>
        get() = _isFocusWearingNumberPopup

    private val _selectedMonthOption: MutableLiveData<Int?> = MutableLiveData<Int?>()
    val selectedMonthOption: LiveData<Int?>
        get() = _selectedMonthOption

    private val _selectedWearingNumberOption: MutableLiveData<Int?> = MutableLiveData<Int?>()
    val selectedWearingNumberOption: LiveData<Int?>
        get() = _selectedWearingNumberOption

    private val _recommendWearingNumberOfWeek: MutableLiveData<Int?> = MutableLiveData<Int?>()
    val recommendWearingNumberOfWeek: LiveData<Int?>
        get() = _recommendWearingNumberOfWeek

    private val _recommendWearingNumberOfMonth: MutableLiveData<Int?> = MutableLiveData<Int?>()
    val recommendWearingNumberOfMonth: LiveData<Int?>
        get() = _recommendWearingNumberOfMonth

    private val _selectedSeason: MutableLiveData<String> = MutableLiveData<String>()
    val selectedSeason: LiveData<String>
        get() = _selectedSeason

    private val _selectedSeasonId: MutableLiveData<Int> = MutableLiveData<Int>()
    val selectedSeasonId: LiveData<Int>
        get() = _selectedSeasonId

    private val _selectedClothCategoryId: MutableLiveData<Int> = MutableLiveData<Int>(0)
    val selectedClothCategoryId: LiveData<Int>
        get() = _selectedClothCategoryId

    private val _registeredClothId: MutableLiveData<Event<Long>> =
        MutableLiveData<Event<Long>>()
    val registeredClothId: LiveData<Event<Long>>
        get() = _registeredClothId

    // 옷 재등록

    private val _requestedFixClothInfo: MutableLiveData<Event<ResponseRegisteredClothInfo>> =
        MutableLiveData<Event<ResponseRegisteredClothInfo>>()
    val requestedFixClothInfo: LiveData<Event<ResponseRegisteredClothInfo>>
        get() = _requestedFixClothInfo

    private val _isRequestedFixCloth: MutableLiveData<Event<Boolean>> =
        MutableLiveData<Event<Boolean>>()
    val isRequestedFixCloth: LiveData<Event<Boolean>>
        get() = _isRequestedFixCloth

    private val _isRequestedResetCompletedCloth: MutableLiveData<Event<Boolean>> =
        MutableLiveData<Event<Boolean>>()
    val isRequestedResetCompletedCloth: LiveData<Event<Boolean>>
        get() = _isRequestedResetCompletedCloth

    private val _isSuccessUpdatingClothInfo: MutableLiveData<Event<Boolean>> =
        MutableLiveData<Event<Boolean>>()
    val isSuccessUpdatingClothInfo: LiveData<Event<Boolean>>
        get() = _isSuccessUpdatingClothInfo

    fun requestRegisteringCloth(imageFile: File?, clothId: Int?) {
        if (clothId != null) {
            if (_isRequestedFixCloth.value!!.content) {
                requestFixClothToServer(clothId)
            } else if(_isRequestedResetCompletedCloth.value!!.content) {
                requestResetClothToServer(clothId)
            }
        } else if(imageFile != null) {
            addNewCloth(imageFile)
        }
    }

    // 서버 호출

    private fun addNewCloth(imageFile: File) {
        viewModelScope.launch {
            try {
                val request = GsonBuilder().serializeNulls().create().toJson(
                    RequestAddNewCloth(
                        _selectedClothCategoryId.value!!,
                        _selectedSeasonId.value!!,
                        _selectedWearingNumberOption.value,
                        _selectedMonthOption.value,
                        _isValidInvalidSeasonConfirm.value!! && !_isNegativeInvalidSeasonConfirm.value!!,
                        _recommendWearingNumberOfMonth.value,
                        _recommendWearingNumberOfWeek.value
                    )
                )
                val body = request.toRequestBody("application/json".toMediaType())
                val multipartBody = MultipartBody.Part.createFormData(
                    name = "image",
                    filename = imageFile.name,
                    body = imageFile.asRequestBody("image/*".toMediaType())
                )

                Timber.d("옷 등록 리퀘스트 : $request")

                val response = repository.addNewCloth(
                    dataStore.getAccessToken().first(),
                    multipartBody,
                    body
                )
                response.enqueue(object : Callback<Long> {
                    override fun onResponse(
                        call: Call<Long>,
                        response: Response<Long>
                    ) {
                        if (response.isSuccessful) {
                            _registeredClothId.value = Event(response.body()!!)
                            Timber.d("옷 등록 성공 : ${_registeredClothId.value}")
                        } else {
                            Timber.d("옷 등록 실패1 : ${(response.errorBody().toString())}")
                        }
                    }

                    override fun onFailure(call: Call<Long>, t: Throwable) {
                        Timber.d("옷 등록 실패2 : $t")
                    }
                })
            } catch (e: Throwable) {
                Timber.d(e)
            }
        }
    }

    private fun requestFixClothToServer(clothId: Int) {
        viewModelScope.launch {
            try {
                val request = RequestAddNewCloth(
                    _selectedClothCategoryId.value!!,
                    _selectedSeasonId.value!!,
                    _selectedWearingNumberOption.value,
                    _selectedMonthOption.value,
                    _isValidInvalidSeasonConfirm.value!! && !_isNegativeInvalidSeasonConfirm.value!!,
                    _recommendWearingNumberOfMonth.value,
                    _recommendWearingNumberOfWeek.value
                )
                val response =
                    repository.fixClothItem(dataStore.getAccessToken().first(), request, clothId)
                response.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.code() == 200) {
                            _isSuccessUpdatingClothInfo.value = Event(true)
                            Timber.d("옷 수정 성공")
                        } else {
                            _isSuccessUpdatingClothInfo.value = Event(false)
                            Timber.d("옷 수정 요청 실패1 - ${response.errorBody()}")
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Timber.d("옷 수정 요청 실패2 - $t")
                    }

                })
            } catch (e: Throwable) {
                Timber.d(e)
            }
        }
    }

    private fun requestResetClothToServer(clothId: Int) {
        viewModelScope.launch {
            try {
                val request = RequestResetCompletedCloth(
                    _selectedSeasonId.value!!,
                    _selectedWearingNumberOption.value,
                    _selectedMonthOption.value,
                    _isValidInvalidSeasonConfirm.value!! && !_isNegativeInvalidSeasonConfirm.value!!,
                    _recommendWearingNumberOfMonth.value,
                    _recommendWearingNumberOfWeek.value
                )
                val response = repository.resetCompletedCloth(
                    dataStore.getAccessToken().first(),
                    request,
                    clothId
                )
                response.enqueue(object: Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if(response.code() == 200) {
                            _isSuccessUpdatingClothInfo.value = Event(true)
                            Timber.d("목표 재설정 성공")
                        } else {
                            _isSuccessUpdatingClothInfo.value = Event(false)
                            Timber.d("목표 재설정 요청 실패1 - ${response.errorBody()}")
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Timber.d("목표 재설정 요청 실패2 - $t")
                    }

                })
            } catch (e: Throwable) {
                Timber.d(e)
            }
        }
    }

    fun fixClothInfo(clothId: Int, isCompletedCloth: Boolean) {
        viewModelScope.launch {
            try {
                val response =
                    repository.getRegisteredClothInfo(dataStore.getAccessToken().first(), clothId)
                response.enqueue(object : Callback<ResponseRegisteredClothInfo> {
                    override fun onResponse(
                        call: Call<ResponseRegisteredClothInfo>,
                        response: Response<ResponseRegisteredClothInfo>
                    ) {
                        if (response.isSuccessful) {
                            _isRequestedResetCompletedCloth.value = Event(isCompletedCloth)
                            _isRequestedFixCloth.value = Event(!isCompletedCloth)
                            _requestedFixClothInfo.value = Event(response.body()!!)
                            Timber.d("등록된 옷 정보 수정 요청 성공 - ${response.body()}")
                        } else {
                            _isRequestedResetCompletedCloth.value = Event(false)
                            _isRequestedFixCloth.value = Event(false)
                            Timber.d("등록된 옷 정보 수정 요청 실패1 - ${response.errorBody()}")
                        }
                    }

                    override fun onFailure(call: Call<ResponseRegisteredClothInfo>, t: Throwable) {
                        Timber.d("등록된 옷 정보 수정 요청 실패2 - $t")
                    }

                })
            } catch (e: Throwable) {
                Timber.d(e)
            }
        }
    }

    // 내부 처리

    fun checkSeasonValidation(selectedSeason: String, seasonList: List<String>) {
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
        val isValidSeason: Boolean =
            when (selectedSeason) {
                seasonList[0] -> {
                    _selectedSeason.value = "봄 · 가을"
                    _selectedSeasonId.value = 0
                    currentMonth in 3..5 || currentMonth in 9..11
                }

                seasonList[1] -> {
                    _selectedSeason.value = "여름"
                    _selectedSeasonId.value = 1
                    currentMonth in 6..8
                }

                else -> {
                    _selectedSeason.value = "겨울"
                    _selectedSeasonId.value = 2
                    currentMonth in 1..2 || currentMonth == 12
                }
            }
        Timber.d("계절 선택 : ${_selectedSeasonId.value}")
        initClothWearingGoalOptionStatus(isValidSeason)
        initRecommendWearingStatus(false)
        initInvalidSeasonConfirmStatus(!isValidSeason)
        initNegativeInvalidSeasonConfirmStatus(false)
    }

    fun checkInvalidSeasonConfirmResponse(selectedResponse: String, confirmResponse: List<String>) {
        val isPositive = when (selectedResponse) {
            confirmResponse[0] -> true
            else -> false
        }
        initClothWearingGoalOptionStatus(isPositive)
        initRecommendWearingStatus(false)
        initNegativeInvalidSeasonConfirmStatus(!isPositive)
    }

    fun setWearingGoalMonthOptionStatus(status: Boolean, option: String) {
        _isFocusMonthPopup.value = status
        _selectedMonthOption.value = option[0].toString().toInt()
        if (status) {
            initWearingGoalNumberOption()
            initRecommendWearingStatus(false)
        }
        if (_selectedWearingNumberOption.value != null && _selectedWearingNumberOption.value!! > 0) {
            initRecommendWearingStatus(true)
        }
    }

    fun setWearingGoalNumberOptionStatus(status: Boolean, option: String) {
        _isFocusWearingNumberPopup.value = status
        _selectedWearingNumberOption.value = option.toInt()
        if (_selectedMonthOption.value != null && _selectedMonthOption.value!! > 0) {
            initRecommendWearingStatus(true)
        }
    }

    private fun initWearingGoalNumberOption() {
        _isFocusWearingNumberPopup.value = false
        _selectedWearingNumberOption.value = null
    }

    private fun initWearingGoalMonthOption() {
        _isFocusMonthPopup.value = false
        _selectedMonthOption.value = null
    }

    private fun initClothWearingGoalOptionStatus(status: Boolean) {
        _isValidShowingWearingGoal.value = status
        initWearingGoalMonthOption()
        initWearingGoalNumberOption()
    }

    private fun initRecommendWearingStatus(status: Boolean) {
        _isValidShowingRecommendWearing.value = status
        if (status) {
            _recommendWearingNumberOfMonth.value =
                _selectedWearingNumberOption.value!! / _selectedMonthOption.value!!
            _recommendWearingNumberOfWeek.value = _recommendWearingNumberOfMonth.value!! / 4
        } else {
            _recommendWearingNumberOfMonth.value = null
            _recommendWearingNumberOfWeek.value = null
        }
    }

    private fun initInvalidSeasonConfirmStatus(status: Boolean) {
        _isValidInvalidSeasonConfirm.value = status
    }

    private fun initNegativeInvalidSeasonConfirmStatus(status: Boolean) {
        _isNegativeInvalidSeasonConfirm.value = status
    }

    fun initAllStatus() {
        initInvalidSeasonConfirmStatus(false)
        initClothWearingGoalOptionStatus(false)
        initRecommendWearingStatus(false)
        initNegativeInvalidSeasonConfirmStatus(false)
        _selectedClothCategoryId.value = 0
    }

    fun setClothCategory(categoryList: List<String>, selectedCategory: String) {
        _selectedClothCategoryId.value = categoryList.indexOf(selectedCategory)
        Timber.d("옷 카테고리 선택 : ${_selectedClothCategoryId.value}")
    }
}