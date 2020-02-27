package com.luanbarbosagomes.hmr.feature.add

import androidx.lifecycle.MutableLiveData
import com.luanbarbosagomes.hmr.App.Companion.database
import com.luanbarbosagomes.hmr.SaveStatus
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.Level
import com.luanbarbosagomes.hmr.data.repository.ExpressionRepository
import com.luanbarbosagomes.hmr.feature.BaseViewModel

class NewExpressionViewModel : BaseViewModel() {

    private val expressionRepository by lazy { ExpressionRepository(database) }

    val status: MutableLiveData<SaveStatus> = MutableLiveData()

    override fun onError(throwable: Throwable) = status.postValue(SaveStatus.FAILED)

    fun saveExpression(expression: String, translation: String) {
        launch {
            expressionRepository.save(
                Expression(
                    value = expression,
                    translation = translation,
                    level = Level.NEW // TODO - let the user device this
                )
            )
            status.postValue(SaveStatus.SAVED)
        }
    }

}
