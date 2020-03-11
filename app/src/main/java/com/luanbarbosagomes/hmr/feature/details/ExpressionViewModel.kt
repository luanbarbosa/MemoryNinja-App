package com.luanbarbosagomes.hmr.feature.details

import androidx.lifecycle.MutableLiveData
import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.repository.ExpressionRepository
import com.luanbarbosagomes.hmr.feature.BaseViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpressionViewModel : BaseViewModel() {

    init {
        App.daggerMainComponent.inject(this)
    }

    @Inject
    lateinit var  expressionRepository: ExpressionRepository

    val state: MutableLiveData<State> = MutableLiveData()

    override fun onError(throwable: Throwable) = state.postValue(State.Error(throwable))

    fun retrieveExpression(id: Long) {
        launch {
            expressionRepository.get(id)?.let {
                state.postValue(State.Success(it))
            }
        }
    }

    // TODO - temporary code ----------------------------
    fun deleteAll() {
        launch {
            expressionRepository.deleteAll()
        }
    }
    // TODO - temporary code ----------------------------

    sealed class State {
        data class Success(val expression: Expression): State()
        data class Error(val error: Throwable): State()
    }
}
