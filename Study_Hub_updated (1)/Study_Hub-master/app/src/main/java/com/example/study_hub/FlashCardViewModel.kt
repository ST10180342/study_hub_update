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
data class Flashcard(
    val id: String? = null,
    val user_id: String,
    val subject: String,
    val question: String,
    val answer: String
)

class FlashcardViewModel : ViewModel() {

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _flashcards =
        MutableStateFlow<List<Flashcard>>(emptyList())

    val flashcards: StateFlow<List<Flashcard>> =
        _flashcards.asStateFlow()

    fun loadFlashcards() {
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
                    .from("flashcards")
                    .select {
                        filter {
                            eq("user_id", user.id)
                        }
                    }
                    .decodeList<Flashcard>()

                _flashcards.value = result

            } catch (e: Exception) {

                _message.value =
                    e.message ?: "Failed to load flashcards."

            } finally {

                _isLoading.value = false
            }
        }
    }

    fun saveFlashcard(
        subject: String,
        question: String,
        answer: String
    ) {

        if (subject.isBlank()) {
            _message.value = "Please enter the subject."
            return
        }

        if (question.isBlank()) {
            _message.value = "Please enter the question."
            return
        }

        if (answer.isBlank()) {
            _message.value = "Please enter the answer."
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

                val flashcard = Flashcard(
                    user_id = user.id,
                    subject = subject,
                    question = question,
                    answer = answer
                )

                supabase
                    .from("flashcards")
                    .insert(flashcard)

                _message.value =
                    "Flashcard saved successfully!"

                loadFlashcards()

            } catch (e: Exception) {

                _message.value =
                    e.message ?: "Failed to save flashcard."

            } finally {

                _isLoading.value = false
            }
        }
    }

    fun clearMessage() {
        _message.value = ""
    }
}