package com.example.refit.presentation.closet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.refit.data.model.closet.ForestStampResponse
import com.example.refit.data.repository.colset.ClosetRepository
import timber.log.Timber

class ForestViewModel(private val repository: ClosetRepository): ViewModel() {

    private val _forestStamp: MutableLiveData<List<ForestStampResponse>> =
        MutableLiveData<List<ForestStampResponse>>()
    val forestStamp: LiveData<List<ForestStampResponse>>
        get() = _forestStamp

    fun getForestStampStatus() {
        try {
            _forestStamp.value = listOf(
                ForestStampResponse(0, ""),
                ForestStampResponse(1, ""),
                ForestStampResponse(2, ""),
                ForestStampResponse(3, ""),
                ForestStampResponse(4, ""),
                ForestStampResponse(5, ""),
                ForestStampResponse(6, ""),
                ForestStampResponse(8, ""),
                ForestStampResponse(9, ""),
                ForestStampResponse(10, ""),
                ForestStampResponse(11, ""),
                ForestStampResponse(12, ""),
                ForestStampResponse(13, ""),
                ForestStampResponse(14, ""),
                ForestStampResponse(15, ""),
                ForestStampResponse(16, ""),
            )
        } catch (e: Throwable) {
            Timber.d("숲 현황 데이터 불러오기 실패 : $e")
        }
    }
}