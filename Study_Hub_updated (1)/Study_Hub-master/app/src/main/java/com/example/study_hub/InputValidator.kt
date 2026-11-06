package com.example.study_hub

/** Pure input-validation rules (no Android dependencies, so they are unit-testable). */
object InputValidator {
    const val MIN_PASSWORD = 6

    /** @return an error message, or null when the input is valid. */
    fun validateRegistration(name: String, email: String, password: String, confirm: String): String? = when {
        name.isBlank() -> "Please enter your full name."
        email.isBlank() -> "Please enter your email."
        !isValidEmail(email) -> "Please enter a valid email address."
        password.length < MIN_PASSWORD -> "Password must be at least $MIN_PASSWORD characters."
        password != confirm -> "Passwords do not match."
        else -> null
    }

    fun validateLogin(email: String, password: String): String? = when {
        email.isBlank() -> "Please enter your email."
        !isValidEmail(email) -> "Please enter a valid email address."
        password.isBlank() -> "Please enter your password."
        else -> null
    }

    fun validateAssignment(title: String, subject: String, dueDate: String): String? = when {
        title.isBlank() -> "Please enter an assignment title."
        subject.isBlank() -> "Please enter a subject."
        dueDate.isBlank() -> "Please enter a due date."
        else -> null
    }

    fun isValidEmail(email: String) = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$").matches(email.trim())
}
