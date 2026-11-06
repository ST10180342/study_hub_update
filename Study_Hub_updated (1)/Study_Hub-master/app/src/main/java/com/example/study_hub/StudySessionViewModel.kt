package com.example.study_hub

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class StudySession(
    val id: String? = null,
    val user_id: String,
    val duration_minutes: Int
)

class StudySessionViewModel : ViewModel() {

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()

    private val _totalMinutes = MutableStateFlow(0)
    val totalMinutes: StateFlow<Int> = _totalMinutes.asStateFlow()

    private val _sessionCount = MutableStateFlow(0)
    val sessionCount: StateFlow<Int> = _sessionCount.asStateFlow()

    fun loadSessionStats() {

        viewModelScope.launch {
            try {
                val user = supabase.auth.currentUserOrNull()

                if (user == null) {
                    return@launch
                }

                val sessions = supabase
                    .from("study_sessions")
                    .select {
                        filter {
                            eq("user_id", user.id)
                        }
                    }
                    .decodeList<StudySession>()

                _sessionCount.value = sessions.size

                _totalMinutes.value =
                    sessions.sumOf { it.duration_minutes }

            } catch (e: Exception) {
                _message.value =
                    e.message ?: "Failed to load study sessions."
            }
        }
    }

    fun saveCompletedSession(durationMinutes: Int) {

        if (durationMinutes <= 0) {
            _message.value = "Invalid study session."
            return
        }

        viewModelScope.launch {
            try {
                val user = supabase.auth.currentUserOrNull()

                if (user == null) {
                    _message.value = "You must be logged in."
                    return@launch
                }

                val session = StudySession(
                    user_id = user.id,
                    duration_minutes = durationMinutes
                )

                supabase
                    .from("study_sessions")
                    .insert(session)

                _sessionCount.value += 1
                _totalMinutes.value += durationMinutes

                _message.value = "Study session saved!"

            } catch (e: Exception) {
                _message.value =
                    e.message ?: "Failed to save study session."
            }
        }
    }

    fun clearMessage() {
        _message.value = ""
    }
}