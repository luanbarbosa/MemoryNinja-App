package com.luanbarbosagomes.hmr.feature.add

import androidx.lifecycle.MutableLiveData
import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.Level
import com.luanbarbosagomes.hmr.data.repository.ExpressionRepository
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
    lateinit var expressionRepository : ExpressionRepository

    val status: MutableLiveData<State> = MutableLiveData()

    override fun onError(throwable: Throwable) =
        status.postValue(State.Error(throwable)).also {
            Timber.w("Unable to add expression!")
            Timber.w(throwable.message ?: "")
        }

    fun saveExpression(expression: String, translation: String) {
        launch {
            expressionRepository.save(
                Expression.create(
                    value = expression,
                    translation = translation,
                    level = Level.NEW // TODO - let the user device this
                )
            )
            status.postValue(State.Success)
        }
    }

    sealed class State {
        object Success : State()
        data class Error(val error: Throwable) : State()
    }

}
