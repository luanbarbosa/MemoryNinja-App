package com.luanbarbosagomes.hmr.feature

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Base ViewModel which handles the launching and canceling of coroutines launched.
 */
abstract class BaseViewModel : ViewModel() {

    private val backgroundScope = CoroutineScope(Dispatchers.IO)
    private val errorHandler = CoroutineExceptionHandler { _, throwable -> localOnError(throwable) }

    abstract fun onError(error: Throwable)

    open fun beforeRun() {}

    open fun afterRun() {}

    /**
     * Asynchronously launch a function using the coroutine scope and handlers defined in this class.
     */
    fun launch(function: suspend CoroutineScope.() -> Unit) =
        backgroundScope.launch(errorHandler) {
            beforeRun()
            function()
            afterRun()
        }

    private fun localOnError(error: Throwable) {
        Timber.e(error)
        onError(error)
    }

}