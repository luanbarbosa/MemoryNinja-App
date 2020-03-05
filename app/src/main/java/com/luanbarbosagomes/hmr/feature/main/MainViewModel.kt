package com.luanbarbosagomes.hmr.feature.main

import androidx.lifecycle.MutableLiveData
import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.feature.BaseViewModel
import com.luanbarbosagomes.hmr.feature.login.AuthViewModel
import com.luanbarbosagomes.hmr.feature.main.MainViewModel.State.*
import timber.log.Timber
import javax.inject.Inject

class MainViewModel : BaseViewModel() {

    @Inject
    internal lateinit var authViewModel: AuthViewModel

    init {
        App.daggerMainComponent.inject(this)
    }

    val state: MutableLiveData<State> = MutableLiveData()

    override fun onError(throwable: Throwable) = state.postValue(Error(throwable))

    fun addExpression() = state.postValue(NewExpression)

    fun listExpressions() = state.postValue(ListExpressions)

    fun logout() {
        authViewModel.logout()
        state.postValue(LoggedOut)
    }

    fun detailExpression(expression: Expression) = state.postValue(DetailExpression(expression))

    fun loggedIn() = state.postValue(LoggedIn)

    sealed class State {
        object NewExpression : State()
        object ListExpressions : State()
        data class DetailExpression(val expression: Expression) : State()
        object LoggedIn : State()
        object LoggedOut : State()
        data class Error(val error: Throwable) : State()
    }
}