package com.luanbarbosagomes.hmr.feature.details

import androidx.lifecycle.MutableLiveData
import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.repository.ExpressionRepository
import com.luanbarbosagomes.hmr.feature.BaseViewModel
import javax.inject.Inject

class ExpressionViewModel : BaseViewModel() {

    init {
        App.daggerMainComponent.inject(this)
    }

    @Inject
    lateinit var  expressionRepository: ExpressionRepository

    val data: MutableLiveData<Result> = MutableLiveData()

    override fun onError(throwable: Throwable) = data.postValue(Result.Error(throwable))

    fun retrieveExpression(id: Long) {
        launch {
            data.postValue(
                Result.Success(expressionRepository.get(id))
            )
        }
    }

    sealed class Result {
        data class Success(val expression: Expression): Result()
        data class Error(val error: Throwable): Result()
    }
}
