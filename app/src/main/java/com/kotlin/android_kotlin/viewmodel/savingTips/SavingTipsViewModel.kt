package com.kotlin.android_kotlin.viewmodel.savingTips

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.android_kotlin.data.savingTips.SavingTips
import com.kotlin.android_kotlin.domain.savingTips.SavingTipsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavingTipsViewModel(private val repository: SavingTipsRepository) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val savingTipsList = MutableLiveData<List<SavingTips>>()

    init {
        savingTipsList.value = emptyList() // Initialize the LiveData with empty list
    }

    fun addSavingTip(savingTip: SavingTips) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repository.addSavingTip(savingTip)
            isLoading.postValue(false)
        }
    }

    fun getAllSavingTips() {
        viewModelScope.launch {
            isLoading.postValue(true)
            withContext(Dispatchers.Default) {
                savingTipsList.postValue(repository.getAllSavingTips())
            }
            isLoading.postValue(false)
        }
    }

    fun updateLikes(savingTipId: String, likes: Int) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repository.likeSavingTip(savingTipId)
            isLoading.postValue(false)
        }
    }

    fun deleteSavingTip(savingTipId: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repository.deleteSavingTip(savingTipId)
            isLoading.postValue(false)
        }
    }

    fun dislikeSavingTip(savingTipId: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repository.dislikeSavingTip(savingTipId)
            isLoading.postValue(false)
        }
    }
}
