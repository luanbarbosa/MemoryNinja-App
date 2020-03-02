package com.luanbarbosagomes.hmr

import android.app.Application
import android.content.Context
import android.text.format.Time
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.luanbarbosagomes.hmr.data.database.AppDatabase
import com.luanbarbosagomes.hmr.work.NotificationWorker
import java.util.concurrent.TimeUnit
import kotlin.time.Duration

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this.applicationContext
        loadDatabase()

        // TODO - make this configurable by the user on settings
        val work = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(appContext).enqueueUniquePeriodicWork(
            "reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            work
        )
    }

    private fun loadDatabase() {
        database = Room
            .databaseBuilder(this, AppDatabase::class.java, "app-db")
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .createFromAsset("database/app-db.db")
            .build()
    }

    companion object {
        lateinit var database: AppDatabase
            private set

        lateinit var appContext: Context
            private set
    }
}
