package com.luanbarbosagomes.hmr.feature.add

import androidx.lifecycle.MutableLiveData
import com.luanbarbosagomes.hmr.App.Companion.database
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.data.Level
import com.luanbarbosagomes.hmr.data.repository.ExpressionRepository
import com.luanbarbosagomes.hmr.feature.BaseViewModel
import com.luanbarbosagomes.hmr.feature.add.NewExpressionStatus.FAILED
import kotlin.coroutines.CoroutineContext

enum class NewExpressionStatus {
    SAVED, FAILED
}

class NewExpressionViewModel : BaseViewModel() {

    private val expressionRepository by lazy { ExpressionRepository(database) }

    val status: MutableLiveData<NewExpressionStatus> = MutableLiveData()

    override fun onError(throwable: Throwable) = status.postValue(FAILED)

    fun saveExpression(expression: String, translation: String) {
        launch {
            expressionRepository.save(
                Expression(
                    value = expression,
                    translation = translation,
                    level = Level.NEW // TODO - let the user device this
                )
            )
            status.postValue(NewExpressionStatus.SAVED)
        }
    }

}
