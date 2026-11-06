package com.example.study_hub

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import com.example.study_hub.data.OfflineRepository

@Serializable
data class Assignment(
    val id: String? = null,
    val user_id: String,
    val title: String,
    val subject: String,
    val due_date: String,
    val description: String? = null
)

class AssignmentViewModel : ViewModel() {

    // Offline-first storage (Room) + sync with Supabase
    private val repo = OfflineRepository()

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _assignments =
        MutableStateFlow<List<Assignment>>(emptyList())

    val assignments: StateFlow<List<Assignment>> =
        _assignments.asStateFlow()

    fun loadAssignments() {

        viewModelScope.launch {

            _isLoading.value = true
            _message.value = ""

            try {

                val user = supabase.auth.currentUserOrNull()

                if (user == null) {
                    _message.value = "You must be logged in."
                    return@launch
                }

                // Push anything saved while offline first, then pull fresh data
                repo.syncPending()

                val result = supabase
                    .from("assignments")
                    .select {
                        filter {
                            eq("user_id", user.id)
                        }
                    }
                    .decodeList<Assignment>()

                repo.cacheAssignments(user.id, result)
                _assignments.value = result

            } catch (e: Exception) {

                // Offline (or server error): fall back to the local Room cache
                Log.w(StudyHubApplication.TAG, "Load failed, using offline cache", e)
                val user = supabase.auth.currentUserOrNull()
                if (user != null) {
                    _assignments.value = repo.localAssignments(user.id)
                    _message.value = "Offline mode: showing saved assignments."
                } else {
                    _message.value = e.message ?: "Failed to load assignments."
                }

            } finally {

                _isLoading.value = false
            }
        }
    }

    fun saveAssignment(
        title: String,
        subject: String,
        dueDate: String,
        description: String
    ) {

        InputValidator.validateAssignment(title, subject, dueDate)?.let {
            _message.value = it
            return
        }

        viewModelScope.launch {

            _isLoading.value = true
            _message.value = ""

            try {

                val user = supabase.auth.currentUserOrNull()

                if (user == null) {
                    _message.value = "You must be logged in."
                    return@launch
                }

                val assignment = Assignment(
                    user_id = user.id,
                    title = title,
                    subject = subject,
                    due_date = dueDate,
                    description = description.ifBlank { null }
                )

                try {
                    supabase
                        .from("assignments")
                        .insert(assignment)

                    _message.value =
                        "Assignment saved successfully!"
                } catch (e: Exception) {
                    // No connection: store locally and sync automatically when back online
                    Log.w(StudyHubApplication.TAG, "Insert failed, saving offline", e)
                    repo.saveOffline(assignment)
                    _assignments.value = repo.localAssignments(user.id)
                    _message.value =
                        "You are offline. Assignment saved on this device and will sync automatically."
                    return@launch
                }

                loadAssignments()

            } catch (e: Exception) {

                Log.e(StudyHubApplication.TAG, "Save assignment failed", e)
                _message.value =
                    e.message ?: "Failed to save assignment."

            } finally {

                _isLoading.value = false
            }
        }
    }

    fun clearMessage() {
        _message.value = ""
    }
}