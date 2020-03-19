package com.luanbarbosagomes.hmr.feature.expression.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.Level
import com.luanbarbosagomes.hmr.data.repository.BaseExpressionRepository
import com.luanbarbosagomes.hmr.feature.BaseViewModel
import javax.inject.Inject

class EditExpressionViewModel : BaseViewModel() {

    init {
        App.daggerMainComponent.inject(this)
    }

    @Inject
    lateinit var expressionRepository : BaseExpressionRepository

    private val _state: MutableLiveData<State> = MutableLiveData()

    val state: LiveData<State>
        get() = _state

    lateinit var editingExpression: Expression

    override fun onError(error: Throwable) =
        _state.postValue(
            State.Error(
                error
            )
        )

    fun loadEditExpression(expression: Expression) {
        editingExpression = expression
        _state.postValue(State.Loaded(editingExpression))
    }

    fun updateExpression(expression: String, translation: String) {
        launch {
            expressionRepository.update(
                editingExpression.copy(value = expression, translation = translation)
            )
            _state.postValue(State.Saved)
        }
    }

    sealed class State {
        object Saved : State()
        data class Loaded(val expression: Expression): State()
        data class Error(val error: Throwable) : State()
    }

}
