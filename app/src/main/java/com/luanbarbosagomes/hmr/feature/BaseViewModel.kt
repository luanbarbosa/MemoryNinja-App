package com.luanbarbosagomes.hmr.feature

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

/**
 * Base ViewModel which handles the launching and canceling of coroutines launched.
 */
abstract class BaseViewModel : ViewModel() {

    private val backgroundScope = CoroutineScope(Dispatchers.IO)
    private val errorHandler = CoroutineExceptionHandler { _, throwable -> onError(throwable) }

    abstract fun onError(throwable: Throwable)

    /**
     * Asynchronously launch a function using the coroutine scope and handlers defined in this class.
     */
    fun launch(function: suspend CoroutineScope.() -> Unit) =
        backgroundScope.launch(errorHandler) {
            function()
        }
}