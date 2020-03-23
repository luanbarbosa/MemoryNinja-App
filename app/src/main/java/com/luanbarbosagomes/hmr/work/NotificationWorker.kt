package com.luanbarbosagomes.hmr.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.luanbarbosagomes.hmr.data.repository.QuizRepository
import com.luanbarbosagomes.hmr.utils.NotificationUtils
import javax.inject.Inject

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {

    @Inject
    lateinit var quizRepository: QuizRepository

    override suspend fun doWork(): Result {

        quizRepository.nextQuiz()?.let {
            NotificationUtils.showQuizNotification(it)
        }
        return Result.success()
    }

}