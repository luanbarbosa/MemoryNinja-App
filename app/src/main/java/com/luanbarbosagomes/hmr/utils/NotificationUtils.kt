package com.luanbarbosagomes.hmr.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavDeepLinkBuilder
import com.luanbarbosagomes.hmr.App
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.data.Expression

data class NotificationAction(
    @DrawableRes val icon: Int, val title: String, val pendingIntent: PendingIntent
)

object NotificationUtils {

    private const val ExpressionNotificationId = 12
    const val ExpressionFromNotification = "ExpressionFromNotification"

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
        contentIntent: PendingIntent,
        actions: Array<NotificationAction> = arrayOf()
    ) {
        createNotificationChannel()
        val builder = NotificationCompat.Builder(
            context,
            context.getString(R.string.expression_notification_channel_id)
        ).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(title)
            setContentText(body)
            setContentIntent(contentIntent)
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

    fun showQuizNotification(expression: Expression) {
        val intent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.main_navigation)
            .setDestination(R.id.fragExpressionDetails)
            .setArguments(Bundle().apply { putParcelable("expression", expression) })
            .createPendingIntent()

        showNotification(
            title = context.getString(R.string.notification_title_expression_reminder),
            body = expression.value,
            contentIntent = intent
        )
    }


}
