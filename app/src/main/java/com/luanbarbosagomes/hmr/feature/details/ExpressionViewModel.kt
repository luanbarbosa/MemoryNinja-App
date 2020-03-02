package com.luanbarbosagomes.hmr.feature.details

import androidx.lifecycle.MutableLiveData
import com.luanbarbosagomes.hmr.App.Companion.database
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.repository.ExpressionRepository
import com.luanbarbosagomes.hmr.feature.BaseViewModel

sealed class Result {
    data class Success(val expression: Expression): Result()
    data class Error(val error: Throwable): Result()
}

class ExpressionViewModel : BaseViewModel() {

    private val expressionRepository by lazy { ExpressionRepository(database) }

    val data: MutableLiveData<Result> = MutableLiveData()

    override fun onError(throwable: Throwable) = data.postValue(Result.Error(throwable))

    fun retrieveExpression(id: Long) {
        launch {
            data.postValue(
                Result.Success(expressionRepository.get(id))
            )
        }
    }

}
