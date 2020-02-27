package com.luanbarbosagomes.hmr.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.feature.main.ActivityMain

data class NotificationAction(
    @DrawableRes val icon: Int, val title: String, val pendingIntent: PendingIntent
)

object NotificationUtils {

    private const val ExpressionNotificationId = 12

    private val context: Context
        get() = App.appContext

    private val notificationManager =
        ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

    private fun showNotification(
        title: String,
        body: String,
        actions: Array<NotificationAction>
    ) {
        createNotificationChannel()
        val contentIntent = Intent(context, ActivityMain::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            context, ExpressionNotificationId, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(
            context,
            context.getString(R.string.expression_notification_channel_id)
        ).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(title)
            setContentText(body)
            setContentIntent(contentPendingIntent)
            setAutoCancel(true)
            actions.forEach {
                addAction(it.icon, it.title, it.pendingIntent)
            }
        }
        notificationManager.notify(ExpressionNotificationId, builder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.expression_notification_channel_id)
            val description = context.getString(R.string.expression_notification_channel_explanation)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel  = NotificationChannel(
                context.getString(R.string.expression_notification_channel_id), name, importance
            ).apply { this.description = description }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showExpressionReminderNotification(expression: Expression) {
        showNotification(
            title = context.getString(R.string.notification_title_expression_reminder),
            body = expression.value,
            actions = arrayOf(
                NotificationAction(
                    icon = R.drawable.ic_check,
                    title = context.getString(R.string.expression_notification_action_known),
                    // TODO - mark the expression as known.
                    //  Maybe after a while this word will change level automatically
                    pendingIntent = PendingIntent.getActivity(
                        context,
                        ExpressionNotificationId,
                        Intent(context, ActivityMain::class.java),
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                ),
                NotificationAction(
                    icon = R.drawable.ic_sad,
                    title = context.getString(R.string.expression_notification_action_unknown),
                    pendingIntent = PendingIntent.getActivity(
                        context,
                        ExpressionNotificationId,
                        // TODO - open proper visualizer view
                        Intent(context, ActivityMain::class.java),
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
            )
        )
    }


}
