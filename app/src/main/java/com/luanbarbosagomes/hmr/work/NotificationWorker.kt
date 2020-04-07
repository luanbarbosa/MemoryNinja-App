package com.luanbarbosagomes.hmr.work

import android.content.Context
import androidx.work.*
import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.dagger.RepositoryModule
import com.luanbarbosagomes.hmr.data.repository.QuizRepository
import com.luanbarbosagomes.hmr.utils.NotificationUtils
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    init {
        App.daggerMainComponent.inject(this)
    }

    @Inject
    lateinit var quizRepository: QuizRepository

    override suspend fun doWork(): Result {
        quizRepository.nextQuiz()?.let {
            NotificationUtils.showQuizNotification(it)
        }
        return Result.success()
    }

    companion object {

        fun scheduleQuiz(frequency: Long) {
            val work = PeriodicWorkRequestBuilder<NotificationWorker>(
                frequency,
                TimeUnit.HOURS
            ).build()
            WorkManager.getInstance(App.appContext).enqueueUniquePeriodicWork(
                "reminder",
                ExistingPeriodicWorkPolicy.KEEP,
                work
            )
        }
    }
}