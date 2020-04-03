package com.luanbarbosagomes.hmr

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.luanbarbosagomes.hmr.dagger.DaggerMainComponent
import com.luanbarbosagomes.hmr.dagger.MainComponent
import com.luanbarbosagomes.hmr.data.database.AppDatabase
import com.luanbarbosagomes.hmr.work.NotificationWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        appContext = this.applicationContext
        daggerMainComponent = DaggerMainComponent.create()

        loadFirebaseServices()
        loadDatabase()

        val work = PeriodicWorkRequestBuilder<NotificationWorker>(12, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(appContext).enqueueUniquePeriodicWork(
            "reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            work
        )
    }

    private fun loadFirebaseServices() {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDb = Firebase.firestore
    }

    private fun loadDatabase() {
        database = Room
            .databaseBuilder(this, AppDatabase::class.java, "app-db")
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .build()
    }

    companion object {
        lateinit var database: AppDatabase
            private set

        lateinit var appContext: Context
            private set

        lateinit var daggerMainComponent: MainComponent
            private set

        lateinit var firebaseAuth: FirebaseAuth
            private set

        lateinit var firebaseDb: FirebaseFirestore
            private set

        val currentFirebaseUser: FirebaseUser?
            get() = firebaseAuth.currentUser

        var isLoggedIn: Boolean = false
            private set
            get() = currentFirebaseUser != null
    }
}

class UserNotFoundException: Throwable()
