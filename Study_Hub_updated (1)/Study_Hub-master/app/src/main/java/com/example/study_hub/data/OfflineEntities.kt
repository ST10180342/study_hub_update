package com.example.study_hub.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Room entities for offline mode support (Part 3 PoE requirement).
 * Data can be stored locally and later synchronised with Supabase.
 */

@Entity(tableName = "offline_assignments")
data class OfflineAssignment(
    @PrimaryKey val id: String,
    val title: String,
    val dueDate: String? = null,
    val subject: String? = null,
    val completed: Boolean = false,
    val userId: String,
    val synced: Boolean = false,
    val description: String? = null
)

@Entity(tableName = "offline_timetable")
data class OfflineTimetableEntry(
    @PrimaryKey val id: String,
    val subject: String,
    val day: String,
    val startTime: String,
    val endTime: String,
    val room: String? = null,
    val userId: String,
    val synced: Boolean = false
)

@Entity(tableName = "offline_flashcards")
data class OfflineFlashcard(
    @PrimaryKey val id: String,
    val front: String,
    val back: String,
    val deck: String? = null,
    val userId: String,
    val synced: Boolean = false
)

@Dao
interface OfflineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(assignment: OfflineAssignment)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimetable(entry: OfflineTimetableEntry)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcard(card: OfflineFlashcard)

    @Query("SELECT * FROM offline_assignments WHERE userId = :userId")
    suspend fun getAssignments(userId: String): List<OfflineAssignment>

    @Query("SELECT * FROM offline_timetable WHERE userId = :userId")
    suspend fun getTimetable(userId: String): List<OfflineTimetableEntry>

    @Query("SELECT * FROM offline_flashcards WHERE userId = :userId")
    suspend fun getFlashcards(userId: String): List<OfflineFlashcard>

    @Query("SELECT * FROM offline_assignments WHERE synced = 0")
    suspend fun getUnsyncedAssignments(): List<OfflineAssignment>

    /** Replace the cached copy of a user's synced assignments with fresh server data. */
    @Query("DELETE FROM offline_assignments WHERE userId = :userId AND synced = 1")
    suspend fun clearSyncedAssignments(userId: String)

    @Query("UPDATE offline_assignments SET synced = 1 WHERE id = :id")
    suspend fun markAssignmentSynced(id: String)
}

@Database(
    entities = [
        OfflineAssignment::class,
        OfflineTimetableEntry::class,
        OfflineFlashcard::class
    ],
    version = 1,
    exportSchema = false
)
abstract class StudyHubDatabase : RoomDatabase() {
    abstract fun offlineDao(): OfflineDao
}
