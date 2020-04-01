package com.luanbarbosagomes.hmr.work

import android.app.IntentService
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.utils.NotificationUtils

class QuizResponseService : IntentService("QuizResponseService") {

    override fun onHandleIntent(intent: Intent?) {
        NotificationUtils.dismissQuizNotification()
        val quizExpression = intent?.getParcelableExtra<Expression>(QuizExpression)

        if (intent != null && quizExpression != null) {
            when (intent.action) {
                QuizResponseKnow ->
                    scheduleQuizResponseWorker(correctAnswer = true, expression = quizExpression)
                QuizResponseNotKnow ->
                    scheduleQuizResponseWorker(correctAnswer = false, expression = quizExpression)
            }
        }
    }

    private fun scheduleQuizResponseWorker(
        correctAnswer: Boolean,
        expression: Expression
    ) {
        WorkManager
            .getInstance(applicationContext)
            .enqueue(
                OneTimeWorkRequestBuilder<QuizWorker>()
                    .setInputData(
                        workDataOf(
                            QuizWorker.CorrectAnswer to correctAnswer,
                            QuizWorker.ExpressionId to expression.uid
                        )
                    )
                    .build()
            )
    }

    companion object {
        const val QuizResponseKnow = "know"
        const val QuizResponseNotKnow = "not-know"
        const val QuizExpression = "quiz-expression"
    }
}