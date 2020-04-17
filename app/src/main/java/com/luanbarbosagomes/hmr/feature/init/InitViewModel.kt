package com.luanbarbosagomes.hmr.feature.init

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.feature.BaseViewModel
import com.luanbarbosagomes.hmr.feature.preference.PreferenceViewModel
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class InitViewModel @Inject constructor(
    private val preferenceViewModel: PreferenceViewModel
): BaseViewModel() {

    private val _state: MutableLiveData<State> by lazy {
        return@lazy MutableLiveData(nextStep())
    }

    val state: LiveData<State>
        get() = _state

    override fun onError(error: Throwable) = _state.postValue(State.Error(error))

    private fun nextStep() : State = when {
        !preferenceViewModel.isLoggedIn() -> State.LoginNeeded
        preferenceViewModel.storageOptionSet() -> State.Initiated
        else -> State.StorageOptionNeeded
    }

    sealed class State {
        object LoginNeeded: State()
        object StorageOptionNeeded: State()
        object Initiated: State()
        data class Error(val error: Throwable): State()
    }

}