package com.luanbarbosagomes.hmr.feature.expression.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.repository.BaseExpressionRepository
import com.luanbarbosagomes.hmr.data.repository.PreferenceRepository
import com.luanbarbosagomes.hmr.feature.BaseViewModel
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpressionsViewModel @Inject constructor(
    private val expressionRepository : BaseExpressionRepository,
    private val preferenceRepository: PreferenceRepository
): BaseViewModel() {

    private val _state: MutableLiveData<State> by lazy {
        loadMore()
        return@lazy MutableLiveData<State>(State.Loading)
    }

    val state: LiveData<State>
        get() = _state

    var limitReached: Boolean = false

    override fun onError(error: Throwable) = _state.postValue(State.Error(error))

    fun deleteExpression(expression: Expression) {
        launch {
            expressionRepository.delete(expression)
            reload()
        }
    }

    fun reload() {
        launch {
            val expressions = processExpressions(expressionRepository.getAll(paged = false))
            _state.postValue(State.Reloaded(expressions))
        }
    }

    fun loadMore() {
        if (limitReached) return

        launch {
            val moreExpressions = processExpressions(expressionRepository.getAll(paged = true))
            Timber.e("LUAN ${moreExpressions.map { it.value }}")
            limitReached = moreExpressions.isEmpty()
            _state.postValue(State.LoadedMore(moreExpressions))
        }
    }

    private fun processExpressions(expressions: List<Expression>) =
        expressions.filter {
            it.level() in preferenceRepository.filterExpressionBy
        }

    sealed class State {
        data class Error(val error: Throwable): State()
        data class Reloaded(val expressions: List<Expression>): State()
        data class LoadedMore(val expressions: List<Expression>): State()
        object Loading: State()
    }
}
