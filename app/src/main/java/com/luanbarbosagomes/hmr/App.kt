package com.luanbarbosagomes.hmr

import android.app.Application
import androidx.room.Room
import com.luanbarbosagomes.hmr.database.AppDatabase

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        loadDatabase()
    }

    private fun loadDatabase() {
        db = Room.databaseBuilder(
            this, AppDatabase::class.java, "app-db"
        ).build()
    }

    companion object {
        lateinit var db: AppDatabase
            private set

    }
}
