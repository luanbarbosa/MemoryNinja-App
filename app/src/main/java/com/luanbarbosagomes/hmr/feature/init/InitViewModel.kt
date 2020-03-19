package com.luanbarbosagomes.hmr.feature.init

import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.feature.BaseViewModel
import com.luanbarbosagomes.hmr.feature.preference.PreferenceViewModel
import javax.inject.Inject

class InitViewModel: BaseViewModel() {

    init {
        App.daggerMainComponent.inject(this)
    }

    @Inject
    lateinit var preferenceViewModel: PreferenceViewModel

    override fun onError(error: Throwable) {
        TODO("Not yet implemented")
    }

    fun currentState() : State = when {
        !App.isLoggedIn -> State.LoginNeeded
        preferenceViewModel.storageOptionSet() -> State.Initiated
        else -> State.StorageOptionNeeded
    }

    enum class State {
        LoginNeeded, StorageOptionNeeded, Initiated
    }
}