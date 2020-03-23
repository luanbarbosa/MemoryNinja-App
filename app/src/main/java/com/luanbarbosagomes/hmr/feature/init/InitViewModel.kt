package com.luanbarbosagomes.hmr.feature.init

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.feature.BaseViewModel
import com.luanbarbosagomes.hmr.feature.preference.PreferenceViewModel
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InitViewModel: BaseViewModel() {

    init {
        App.daggerMainComponent.inject(this)
    }

    @Inject
    lateinit var preferenceViewModel: PreferenceViewModel

    private val _state: MutableLiveData<State> by lazy {
        init()
        return@lazy MutableLiveData<State>(State.Loading)
    }

    val state: LiveData<State>
        get() = _state

    private fun init() {
        App.firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _state.postValue(nextStep())
            } else {
                Timber.w("Unable to fetch RemoteConfig data! ${task.exception}")
            }
        }
    }

    override fun onError(error: Throwable) = _state.postValue(State.Error(error))

    private fun nextStep() : State = when {
        !App.isLoggedIn -> State.LoginNeeded
        preferenceViewModel.storageOptionSet() -> State.Initiated
        else -> State.StorageOptionNeeded
    }

    sealed class State {
        object Loading: State()
        object LoginNeeded: State()
        object StorageOptionNeeded: State()
        object Initiated: State()
        data class Error(val error: Throwable): State()
    }
}