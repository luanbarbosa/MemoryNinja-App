package com.luanbarbosagomes.hmr

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.luanbarbosagomes.hmr.dagger.DaggerMainComponent
import com.luanbarbosagomes.hmr.dagger.MainComponent
import com.luanbarbosagomes.hmr.data.database.AppDatabase
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        appContext = this.applicationContext
        daggerMainComponent = DaggerMainComponent.create()
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDb = Firebase.firestore

        loadDatabase()

        // TODO - Disabled temporarily! -----------------------------------
        // TODO - make this configurable by the user on settings
//        val work = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.HOURS)
//            .build()
//        WorkManager.getInstance(appContext).enqueueUniquePeriodicWork(
//            "reminder",
//            ExistingPeriodicWorkPolicy.KEEP,
//            work
//        )
        // TODO - Disabled temporarily! -----------------------------------
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
