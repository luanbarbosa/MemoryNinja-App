package com.luanbarbosagomes.hmr.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.data.repository.BaseExpressionRepository
import javax.inject.Inject

class QuizWorker(
    context: Context,
    workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {

    init {
        App.daggerMainComponent.inject(this)
    }

    @Inject
    lateinit var expressionRepository: BaseExpressionRepository

    override suspend fun doWork(): Result {
        val correctAnswer = inputData.getBoolean(CorrectAnswer, true)
        val expressionUid = inputData.getString(ExpressionId)

        expressionUid?.let {
            expressionRepository.updateLevel(it, correctAnswer)
        }
        return Result.success()
    }

    companion object {
        const val CorrectAnswer = "CorrectAnswer"
        const val ExpressionId = "ExpressionId"
    }
}