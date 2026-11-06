package com.example.study_hub.data

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.study_hub.Assignment
import com.example.study_hub.StudyHubApplication
import com.example.study_hub.supabase
import io.github.jan.supabase.postgrest.from
import java.util.UUID

/**
 * Offline mode with sync: assignments created without a connection are stored in Room
 * (synced = false) and uploaded to Supabase by [SyncWorker] once the network returns.
 */
class OfflineRepository {

    private val app get() = StudyHubApplication.instance
    private val dao get() = app.database.offlineDao()

    suspend fun saveOffline(a: Assignment) {
        dao.insertAssignment(
            OfflineAssignment(
                id = a.id ?: UUID.randomUUID().toString(),
                title = a.title, dueDate = a.due_date, subject = a.subject,
                userId = a.user_id, synced = false, description = a.description
            )
        )
        scheduleSync(app)
    }

    suspend fun localAssignments(userId: String): List<Assignment> =
        dao.getAssignments(userId).map {
            Assignment(it.id, it.userId, it.title, it.subject ?: "", it.dueDate ?: "", it.description)
        }

    /** Refresh the local cache with server data, keeping unsynced local rows. */
    suspend fun cacheAssignments(userId: String, remote: List<Assignment>) {
        dao.clearSyncedAssignments(userId)
        remote.forEach {
            dao.insertAssignment(
                OfflineAssignment(
                    id = it.id ?: UUID.randomUUID().toString(), title = it.title,
                    dueDate = it.due_date, subject = it.subject, userId = it.user_id,
                    synced = true, description = it.description
                )
            )
        }
    }

    /** Upload every unsynced row. Returns true when nothing is left pending. */
    suspend fun syncPending(): Boolean {
        var allOk = true
        for (row in dao.getUnsyncedAssignments()) {
            try {
                supabase.from("assignments").insert(
                    Assignment(row.id, row.userId, row.title, row.subject ?: "", row.dueDate ?: "", row.description)
                )
                dao.markAssignmentSynced(row.id)
                Log.d(StudyHubApplication.TAG, "Synced offline assignment ${row.id}")
            } catch (e: Exception) {
                Log.w(StudyHubApplication.TAG, "Sync failed for ${row.id}", e)
                allOk = false
            }
        }
        return allOk
    }

    companion object {
        /** Queue a background sync that only runs when a network connection exists. */
        fun scheduleSync(context: Context) {
            val request = OneTimeWorkRequestBuilder<SyncWorker>()
                .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .build()
            WorkManager.getInstance(context)
                .enqueueUniqueWork("study_hub_sync", ExistingWorkPolicy.REPLACE, request)
        }
    }
}

class SyncWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result =
        if (OfflineRepository().syncPending()) Result.success() else Result.retry()
}
