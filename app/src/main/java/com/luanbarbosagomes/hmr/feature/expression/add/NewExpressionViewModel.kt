package com.luanbarbosagomes.hmr.feature.expression.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.Level
import com.luanbarbosagomes.hmr.data.repository.BaseExpressionRepository
import com.luanbarbosagomes.hmr.feature.BaseViewModel
import com.luanbarbosagomes.hmr.utils.withDelay
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

class NewExpressionViewModel @Inject constructor(
    private val expressionRepository: BaseExpressionRepository
) : BaseViewModel() {

    private val _state = MutableLiveData<State>(State.Input)

    val state: LiveData<State>
        get() = _state

    override fun onError(error: Throwable) =
        _state.postValue(
            State.Error(error)
        ).also {
            Timber.w("Unable to add expression!")
            Timber.w(error.message ?: "")
        }

    fun saveExpression(
        expression: String,
        translation: String,
        level: Level?
    ) {
        launch {
            expressionRepository.save(
                Expression.create(
                    value = expression,
                    translation = translation,
                    level = level ?: Level.NEW
                )
            )
            _state.postValue(State.Success)
        }
    }

    sealed class State {
        object Input : State()
        object Success : State()
        data class Error(val error: Throwable) : State()
    }

}
