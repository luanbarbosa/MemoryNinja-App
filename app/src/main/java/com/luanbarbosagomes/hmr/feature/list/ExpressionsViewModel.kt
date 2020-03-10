package com.luanbarbosagomes.hmr.feature.list

import androidx.lifecycle.MutableLiveData
import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.LoadStatus
import com.luanbarbosagomes.hmr.LoadStatus.FAILED
import com.luanbarbosagomes.hmr.LoadStatus.LOADED
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.repository.ExpressionRepository
import com.luanbarbosagomes.hmr.feature.BaseViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpressionsViewModel : BaseViewModel() {

    init {
        App.daggerMainComponent.inject(this)
    }

    @Inject
    lateinit var expressionRepository : ExpressionRepository

    val state: MutableLiveData<LoadStatus> = MutableLiveData()
    val expressionsData: MutableLiveData<List<Expression>> = MutableLiveData()

    override fun onError(throwable: Throwable) = state.postValue(FAILED)

    fun loadExpressions() {
        launch {
            expressionsData.postValue(expressionRepository.getAll())
            state.postValue(LOADED)
        }
    }

}
