package com.luanbarbosagomes.hmr

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.luanbarbosagomes.hmr.data.database.AppDatabase

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this.applicationContext
        loadDatabase()
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
