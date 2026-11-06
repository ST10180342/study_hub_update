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
data class QuizQuestion(
    val id: String? = null,
    val user_id: String,
    val subject: String,
    val question: String,
    val option_a: String,
    val option_b: String,
    val option_c: String,
    val option_d: String,
    val correct_answer: String
)

class QuizViewModel : ViewModel() {

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _questions =
        MutableStateFlow<List<QuizQuestion>>(emptyList())

    val questions: StateFlow<List<QuizQuestion>> =
        _questions.asStateFlow()

    fun loadQuestions() {

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
                    .from("quiz_questions")
                    .select {
                        filter {
                            eq("user_id", user.id)
                        }
                    }
                    .decodeList<QuizQuestion>()

                _questions.value = result

            } catch (e: Exception) {

                _message.value =
                    e.message ?: "Failed to load quiz questions."

            } finally {

                _isLoading.value = false
            }
        }
    }

    fun saveQuestion(
        subject: String,
        question: String,
        optionA: String,
        optionB: String,
        optionC: String,
        optionD: String,
        correctAnswer: String
    ) {

        if (subject.isBlank()) {
            _message.value = "Please enter the subject."
            return
        }

        if (question.isBlank()) {
            _message.value = "Please enter the question."
            return
        }

        if (optionA.isBlank() ||
            optionB.isBlank() ||
            optionC.isBlank() ||
            optionD.isBlank()
        ) {
            _message.value = "Please enter all four options."
            return
        }

        if (correctAnswer.isBlank()) {
            _message.value = "Please select the correct answer."
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

                val quizQuestion = QuizQuestion(
                    user_id = user.id,
                    subject = subject,
                    question = question,
                    option_a = optionA,
                    option_b = optionB,
                    option_c = optionC,
                    option_d = optionD,
                    correct_answer = correctAnswer
                )

                supabase
                    .from("quiz_questions")
                    .insert(quizQuestion)

                _message.value =
                    "Quiz question saved successfully!"

                loadQuestions()

            } catch (e: Exception) {

                _message.value =
                    e.message ?: "Failed to save quiz question."

            } finally {

                _isLoading.value = false
            }
        }
    }

    fun clearMessage() {
        _message.value = ""
    }
}