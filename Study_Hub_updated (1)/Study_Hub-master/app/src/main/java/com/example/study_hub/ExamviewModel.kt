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
data class Exam(
    val id: String? = null,
    val user_id: String,
    val subject: String,
    val exam_date: String,
    val exam_time: String,
    val venue: String? = null,
    val notes: String? = null
)

class ExamViewModel : ViewModel() {

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _exams =
        MutableStateFlow<List<Exam>>(emptyList())

    val exams: StateFlow<List<Exam>> =
        _exams.asStateFlow()

    fun loadExams() {
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
                    .from("exams")
                    .select {
                        filter {
                            eq("user_id", user.id)
                        }
                    }
                    .decodeList<Exam>()

                _exams.value = result

            } catch (e: Exception) {
                _message.value =
                    e.message ?: "Failed to load exams."

            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveExam(
        subject: String,
        examDate: String,
        examTime: String,
        venue: String,
        notes: String
    ) {

        if (subject.isBlank()) {
            _message.value = "Please enter the subject."
            return
        }

        if (examDate.isBlank()) {
            _message.value = "Please enter the exam date."
            return
        }

        if (examTime.isBlank()) {
            _message.value = "Please enter the exam time."
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

                val exam = Exam(
                    user_id = user.id,
                    subject = subject,
                    exam_date = examDate,
                    exam_time = examTime,
                    venue = venue.ifBlank { null },
                    notes = notes.ifBlank { null }
                )

                supabase
                    .from("exams")
                    .insert(exam)

                _message.value = "Exam saved successfully!"

                loadExams()

            } catch (e: Exception) {

                _message.value =
                    e.message ?: "Failed to save exam."

            } finally {

                _isLoading.value = false
            }
        }
    }

    fun clearMessage() {
        _message.value = ""
    }
}