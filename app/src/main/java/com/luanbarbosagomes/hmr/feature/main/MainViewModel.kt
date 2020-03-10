package com.luanbarbosagomes.hmr.feature.main

import androidx.lifecycle.MutableLiveData
import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.feature.BaseViewModel
import com.luanbarbosagomes.hmr.feature.login.AuthViewModel
import com.luanbarbosagomes.hmr.feature.main.MainViewModel.State.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainViewModel @Inject constructor() : BaseViewModel() {

    @Inject
    internal lateinit var authModel: AuthViewModel

    init {
        App.daggerMainComponent.inject(this)
    }

    val state: MutableLiveData<State> = MutableLiveData(
        if (App.isLoggedIn) LoggedIn else NeedLogin
    )

    override fun onError(throwable: Throwable) = state.postValue(Error(throwable))

    fun addExpression() = state.postValue(NewExpression)

    fun listExpressions() = state.postValue(ListExpressions)

    fun logout() {
        authModel.logout()
        state.postValue(NeedLogin)
    }

    fun detailExpression(expression: Expression) = state.postValue(DetailExpression(expression))

    fun loggedIn() = state.postValue(LoggedIn)

    sealed class State {
        object NewExpression : State()
        object ListExpressions : State()
        data class DetailExpression(val expression: Expression) : State()
        object LoggedIn : State()
        object NeedLogin : State()
        data class Error(val error: Throwable) : State()
    }
}