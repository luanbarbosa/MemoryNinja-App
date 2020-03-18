package com.luanbarbosagomes.hmr.feature.list

import androidx.lifecycle.MutableLiveData
import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.LoadStatus.FAILED
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

    val state: MutableLiveData<State> = MutableLiveData(State.Loading)

    override fun onError(error: Throwable) = state.postValue(State.Error(error))

    fun loadExpressions() {
        launch {
            state.postValue(State.Loaded(expressionRepository.getAll()))
        }
    }

    sealed class State {
        data class Error(val error: Throwable): State()
        data class Loaded(val expressions: List<Expression>): State()
        object Loading: State()
    }
}
