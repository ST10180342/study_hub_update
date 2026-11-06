package com.example.study_hub

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/** Receives Firebase Cloud Messaging pushes (real-time notifications) and shows them. */
class StudyHubMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d(StudyHubApplication.TAG, "New FCM token received")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(StudyHubApplication.TAG, "Push received from ${message.from}")
        if (!SettingsStore.notificationsEnabled(this)) return

        val title = message.notification?.title ?: message.data["title"] ?: "Study Hub"
        val body = message.notification?.body ?: message.data["body"] ?: return

        val granted = android.os.Build.VERSION.SDK_INT < 33 ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        if (!granted) return

        val pending = PendingIntent.getActivity(
            this, 0, Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification = NotificationCompat.Builder(this, StudyHubApplication.CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pending)
            .build()
        NotificationManagerCompat.from(this).notify(System.currentTimeMillis().toInt(), notification)
    }
}
