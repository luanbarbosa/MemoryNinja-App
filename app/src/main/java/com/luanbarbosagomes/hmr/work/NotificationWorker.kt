package com.luanbarbosagomes.hmr.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.data.repository.ExpressionRepository
import com.luanbarbosagomes.hmr.utils.NotificationUtils

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        // TODO - use more intelligence instead of random expression
        ExpressionRepository(App.database).getRandom()?.let {
            NotificationUtils.showExpressionReminderNotification(it)
        }
        return Result.success()
    }

}