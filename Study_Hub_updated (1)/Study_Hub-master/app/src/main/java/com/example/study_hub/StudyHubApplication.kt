package com.example.study_hub

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.room.Room
import com.example.study_hub.data.StudyHubDatabase
import com.google.firebase.messaging.FirebaseMessaging

/**
 * Application entry point. Creates the single Room database used for offline mode,
 * the notification channel used by Firebase Cloud Messaging, and subscribes the
 * device to the "study_hub_updates" topic so real-time pushes can be sent from the
 * Firebase console. (Ref: Firebase Cloud Messaging docs, firebase.google.com/docs/cloud-messaging)
 */
class StudyHubApplication : Application() {

    companion object {
        const val TAG = "StudyHub"
        const val CHANNEL_ID = "study_hub_updates"
        lateinit var instance: StudyHubApplication
            private set
    }

    /** Local SQLite (Room) database - offline-first storage. */
    val database: StudyHubDatabase by lazy {
        Room.databaseBuilder(this, StudyHubDatabase::class.java, "study_hub.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        createNotificationChannel()
        if (SettingsStore.notificationsEnabled(this)) subscribeToUpdates()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "Study Hub updates", NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "Deadline reminders and study alerts" }
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    fun subscribeToUpdates() {
        FirebaseMessaging.getInstance().subscribeToTopic(CHANNEL_ID)
            .addOnCompleteListener { Log.d(TAG, "FCM topic subscribe success=${it.isSuccessful}") }
    }

    fun unsubscribeFromUpdates() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(CHANNEL_ID)
            .addOnCompleteListener { Log.d(TAG, "FCM topic unsubscribe success=${it.isSuccessful}") }
    }
}
