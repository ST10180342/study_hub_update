package com.example.study_hub

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** Unit tests that exercise the real [InputValidator] used by the app's ViewModels. */
class ExampleUnitTest {

    @Test
    fun validRegistration_isAccepted() {
        assertNull(InputValidator.validateRegistration("Study Hub User", "student@example.com", "password123", "password123"))
    }

    @Test
    fun emptyName_isRejected() {
        assertEquals("Please enter your full name.",
            InputValidator.validateRegistration("", "a@b.com", "password123", "password123"))
    }

    @Test
    fun invalidEmail_isRejected() {
        assertFalse(InputValidator.isValidEmail("not-an-email"))
        assertTrue(InputValidator.isValidEmail("a@b.co"))
    }

    @Test
    fun shortPassword_isRejected() {
        assertEquals("Password must be at least 6 characters.",
            InputValidator.validateRegistration("Name", "a@b.com", "12345", "12345"))
    }

    @Test
    fun mismatchedPasswords_areRejected() {
        assertEquals("Passwords do not match.",
            InputValidator.validateRegistration("Name", "a@b.com", "password123", "password456"))
    }

    @Test
    fun loginRequiresEmailAndPassword() {
        assertEquals("Please enter your email.", InputValidator.validateLogin("", "x"))
        assertEquals("Please enter your password.", InputValidator.validateLogin("a@b.com", ""))
        assertNull(InputValidator.validateLogin("a@b.com", "secret1"))
    }

    @Test
    fun assignmentValidation() {
        assertNull(InputValidator.validateAssignment("Essay", "IMAD", "2026-11-20"))
        assertEquals("Please enter an assignment title.", InputValidator.validateAssignment("", "IMAD", "x"))
        assertEquals("Please enter a due date.", InputValidator.validateAssignment("Essay", "IMAD", " "))
    }
}
