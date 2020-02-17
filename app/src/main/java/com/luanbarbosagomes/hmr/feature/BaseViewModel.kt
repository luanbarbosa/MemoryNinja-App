package com.luanbarbosagomes.hmr.feature

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

/**
 * Base ViewModel which handles the launching and canceling of coroutines launched.
 */
abstract class BaseViewModel : ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + viewModelJob)
    private val errorHandler = CoroutineExceptionHandler { _, throwable -> onError(throwable) }

    abstract fun onError(throwable: Throwable)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * Asynchronously launch a function using the coroutine scope and handlers defined in this class.
     */
    fun launch(function: suspend CoroutineScope.() -> Unit) =
        scope.launch(errorHandler) {
            function()
        }
}