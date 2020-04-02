package com.luanbarbosagomes.hmr.feature.expression.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.Level
import com.luanbarbosagomes.hmr.data.repository.BaseExpressionRepository
import com.luanbarbosagomes.hmr.feature.BaseViewModel
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewExpressionViewModel : BaseViewModel() {

    init {
        App.daggerMainComponent.inject(this)
    }

    @Inject
    lateinit var expressionRepository : BaseExpressionRepository

    private val _state: MutableLiveData<State> = MutableLiveData()

    val state: LiveData<State>
        get() = _state

    override fun onError(error: Throwable) =
        _state.postValue(
            State.Error(
                error
            )
        ).also {
            Timber.w("Unable to add expression!")
            Timber.w(error.message ?: "")
        }

    fun saveExpression(expression: String, translation: String) {
        launch {
//            expressionRepository.save(
//                Expression.create(
//                    value = expression,
//                    translation = translation,
//                    level = Level.NEW // TODO - let the user device this
//                )
//            )
            _state.postValue(State.Success)
        }
    }

    sealed class State {
        object Success : State()
        data class Error(val error: Throwable) : State()
    }

}
