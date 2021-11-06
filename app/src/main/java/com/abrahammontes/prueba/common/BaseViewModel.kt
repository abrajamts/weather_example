package com.abrahammontes.prueba.common

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(app: Application) : AndroidViewModel(app), CoroutineScope {

    private val viewModelJob = SupervisorJob()

    override val coroutineContext = Dispatchers.Main + viewModelJob

    protected val _loading = MutableLiveData<Event<Boolean>>()
    protected val _error = MutableLiveData<Event<String>>()

    val loading: LiveData<Event<Boolean>> = _loading
    val error: LiveData<Event<String>> = _error

    fun launchData(block: suspend () -> Unit): Job {
        return launch {
            try {
                _loading.value = Event(true)
                block()
            } catch (error: Throwable) {
                Log.e(TAG, error.message, error.cause)
                _error.value = Event(getMessageForException(error))
            } finally {
                _loading.value = Event(false)
            }
        }
    }

    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }

    companion object {
        const val TAG = "BaseViewModel"
    }
}