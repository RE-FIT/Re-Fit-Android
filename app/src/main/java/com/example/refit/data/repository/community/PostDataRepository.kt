package com.example.refit.data.repository.community

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.refit.data.model.community.PostResponse
import timber.log.Timber

object PostDataRepository {
    private val _postResponse: MutableLiveData<PostResponse> = MutableLiveData<PostResponse>()
    val postResponse: LiveData<PostResponse>
        get() = _postResponse

    fun updatePostResponse(response: PostResponse) {
        _postResponse.postValue(response)
    }
}