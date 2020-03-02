package com.luanbarbosagomes.hmr

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.luanbarbosagomes.hmr.dagger.DaggerMainComponent
import com.luanbarbosagomes.hmr.dagger.MainComponent
import com.luanbarbosagomes.hmr.data.database.AppDatabase
import com.luanbarbosagomes.hmr.work.NotificationWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this.applicationContext
        daggerMainComponent = DaggerMainComponent.create()
        Timber.plant(Timber.DebugTree())

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

        lateinit var daggerMainComponent: MainComponent
            private set
    }
}
