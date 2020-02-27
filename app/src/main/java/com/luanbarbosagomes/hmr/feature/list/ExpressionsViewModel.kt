package com.luanbarbosagomes.hmr.feature.list

import androidx.lifecycle.MutableLiveData
import com.luanbarbosagomes.hmr.App.Companion.database
import com.luanbarbosagomes.hmr.LoadStatus
import com.luanbarbosagomes.hmr.LoadStatus.FAILED
import com.luanbarbosagomes.hmr.LoadStatus.LOADED
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.repository.ExpressionRepository
import com.luanbarbosagomes.hmr.feature.BaseViewModel

class ExpressionsViewModel : BaseViewModel() {

    private val expressionRepository by lazy { ExpressionRepository(database) }

    val status: MutableLiveData<LoadStatus> = MutableLiveData()
    val expressionsData: MutableLiveData<List<Expression>> = MutableLiveData()

    override fun onError(throwable: Throwable) = status.postValue(FAILED)

    fun loadExpressions() {
        launch {
            expressionsData.postValue(expressionRepository.getAll())
            status.postValue(LOADED)
        }
    }

}
