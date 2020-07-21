package com.luanbarbosagomes.hmr.feature.expression.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.repository.BaseExpressionRepository
import com.luanbarbosagomes.hmr.data.repository.PreferenceRepository
import com.luanbarbosagomes.hmr.feature.BaseViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpressionsViewModel @Inject constructor(
    private val expressionRepository : BaseExpressionRepository,
    private val preferenceRepository: PreferenceRepository
): BaseViewModel() {

    private val _state: MutableLiveData<State> by lazy {
        reload()
        return@lazy MutableLiveData<State>(State.Loading)
    }

    val state: LiveData<State>
        get() = _state

    override fun onError(error: Throwable) = _state.postValue(State.Error(error))

    fun deleteExpression(expression: Expression) {
        launch {
            expressionRepository.delete(expression)
            reload()
        }
    }

    fun reload() {
        launch {
            val filterByLevel = preferenceRepository.filterExpressionBy
            val expressions = expressionRepository
                .getAll()
                .filter { it.level() in filterByLevel }

            _state.postValue(State.Loaded(expressions))
        }
    }

    sealed class State {
        data class Error(val error: Throwable): State()
        data class Loaded(val expressions: List<Expression>): State()
        object Loading: State()
    }
}
