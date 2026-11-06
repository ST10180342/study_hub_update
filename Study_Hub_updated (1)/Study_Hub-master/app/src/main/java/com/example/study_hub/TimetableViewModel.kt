
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
data class TimetableEntry(
    val id: String? = null,
    val user_id: String,
    val subject: String,
    val day: String,
    val start_time: String,
    val end_time: String,
    val room: String? = null
)

class TimetableViewModel : ViewModel() {

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _entries = MutableStateFlow<List<TimetableEntry>>(emptyList())
    val entries: StateFlow<List<TimetableEntry>> = _entries.asStateFlow()

    fun loadTimetable() {

        viewModelScope.launch {

            _isLoading.value = true
            _message.value = ""

            try {

                val user = supabase.auth.currentUserOrNull()

                if (user == null) {
                    _message.value = "You must be logged in."
                    return@launch
                }

                val result = supabase
                    .from("timetable")
                    .select {
                        filter {
                            eq("user_id", user.id)
                        }
                    }
                    .decodeList<TimetableEntry>()

                _entries.value = result

            } catch (e: Exception) {

                _message.value =
                    e.message ?: "Failed to load timetable."

            } finally {

                _isLoading.value = false
            }
        }
    }

    fun saveTimetableEntry(
        subject: String,
        day: String,
        startTime: String,
        endTime: String,
        room: String
    ) {

        if (subject.isBlank()) {
            _message.value = "Please enter a subject."
            return
        }

        if (day.isBlank()) {
            _message.value = "Please enter a day."
            return
        }

        if (startTime.isBlank()) {
            _message.value = "Please enter a start time."
            return
        }

        if (endTime.isBlank()) {
            _message.value = "Please enter an end time."
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

                val entry = TimetableEntry(
                    user_id = user.id,
                    subject = subject,
                    day = day,
                    start_time = startTime,
                    end_time = endTime,
                    room = room.ifBlank { null }
                )

                supabase
                    .from("timetable")
                    .insert(entry)

                _message.value = "Timetable entry saved successfully!"

                loadTimetable()

            } catch (e: Exception) {

                _message.value =
                    e.message ?: "Failed to save timetable entry."

            } finally {

                _isLoading.value = false
            }
        }
    }

    fun clearMessage() {
        _message.value = ""
    }
}