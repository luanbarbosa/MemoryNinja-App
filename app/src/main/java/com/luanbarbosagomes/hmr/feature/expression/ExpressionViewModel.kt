package com.luanbarbosagomes.hmr.feature.expression

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.Level
import com.luanbarbosagomes.hmr.data.copy
import com.luanbarbosagomes.hmr.data.repository.BaseExpressionRepository
import com.luanbarbosagomes.hmr.feature.BaseViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpressionViewModel : BaseViewModel() {

    init {
        App.daggerMainComponent.inject(this)
    }

    @Inject
    lateinit var expressionRepository: BaseExpressionRepository

    private val _state = MutableLiveData<State>(State.Loading)

    val state: LiveData<State>
        get() = _state

    val currentExpression: Expression?
        get() = when (val currentState = state.value) {
            is State.Loaded -> currentState.expression
            else -> null
        }

    override fun onError(error: Throwable) = _state.postValue(State.Error(error))

    fun loadExpression(uid: String) {
        launch {
            expressionRepository.get(uid)?.let {
                _state.postValue(State.Loaded(it))
            }
        }
    }

    fun updateExpression(
        expression: String,
        translation: String,
        level: Level?
    ) {
        launch {
            currentExpression?.let {
                expressionRepository.update(
                    it.copy(
                        value = expression,
                        translation = translation,
                        currentLevel = level?.threshold ?: it.currentLevel
                    )
                )
            }
            _state.postValue(State.Saved)
        }
    }

    sealed class State {
        object Saved : State()
        object Loading : State()
        data class Loaded(val expression: Expression) : State()
        data class Error(val error: Throwable) : State()
    }
}
