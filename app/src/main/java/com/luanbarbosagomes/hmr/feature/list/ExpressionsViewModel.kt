package com.luanbarbosagomes.hmr.feature.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.repository.BaseExpressionRepository
import com.luanbarbosagomes.hmr.feature.BaseViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpressionsViewModel : BaseViewModel() {

    init {
        App.daggerMainComponent.inject(this)
    }

    @Inject
    lateinit var expressionRepository : BaseExpressionRepository

    private val _state: MutableLiveData<State> = MutableLiveData(State.Loading)

    val state: LiveData<State>
        get() = _state

    override fun onError(error: Throwable) = _state.postValue(State.Error(error))

    fun loadExpressions() {
        launch {
            _state.postValue(State.Loaded(expressionRepository.getAll()))
        }
    }

    fun deleteExpression(expression: Expression) {
        launch {
            expressionRepository.delete(expression)
            _state.postValue(State.Deleted(expression))
        }
    }

    sealed class State {
        data class Error(val error: Throwable): State()
        data class Loaded(val expressions: List<Expression>): State()
        data class Deleted(val expression: Expression): State()
        object Loading: State()
    }
}
