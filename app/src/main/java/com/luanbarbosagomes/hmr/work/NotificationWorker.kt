package com.luanbarbosagomes.hmr.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.luanbarbosagomes.hmr.data.repository.BaseExpressionRepository
import com.luanbarbosagomes.hmr.utils.NotificationUtils
import javax.inject.Inject

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {

    @Inject
    lateinit var expressionRepository: BaseExpressionRepository

    override suspend fun doWork(): Result {
        // TODO - use more intelligence instead of random expression
        expressionRepository.getRandom()?.let {
            NotificationUtils.showExpressionReminderNotification(it)
        }
        return Result.success()
    }

}