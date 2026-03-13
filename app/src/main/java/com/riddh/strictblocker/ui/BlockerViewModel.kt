package com.riddh.strictblocker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.riddh.strictblocker.data.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BlockerViewModel(private val repository: BlockerRepository) : ViewModel() {

    val blockedApps: StateFlow<List<BlockedApp>> = repository.allApps.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val blockedUrls: StateFlow<List<BlockedUrl>> = repository.allUrls.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val blockedKeywords: StateFlow<List<BlockedKeyword>> = repository.allKeywords.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addApp(packageName: String, appName: String, deadline: Long = 0L) = viewModelScope.launch {
        repository.insertApp(BlockedApp(packageName, appName, deadline))
    }

    fun removeApp(packageName: String, appName: String) = viewModelScope.launch {
        repository.deleteApp(BlockedApp(packageName, appName))
    }

    fun addUrl(url: String, deadline: Long = 0L) = viewModelScope.launch {
        repository.insertUrl(BlockedUrl(url, deadline))
    }

    fun removeUrl(url: String) = viewModelScope.launch {
        repository.deleteUrl(BlockedUrl(url))
    }

    fun addKeyword(keyword: String, deadline: Long = 0L) = viewModelScope.launch {
        repository.insertKeyword(BlockedKeyword(keyword, deadline))
    }

    fun removeKeyword(keyword: String) = viewModelScope.launch {
        repository.deleteKeyword(BlockedKeyword(keyword))
    }
}

class BlockerViewModelFactory(private val repository: BlockerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BlockerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BlockerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
