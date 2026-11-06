package com.example.study_hub

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val TAG = StudyHubApplication.TAG


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()

    fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        onSuccess: () -> Unit
    ) {
        // Validate before touching the network (prevents crashes / bad requests)
        InputValidator.validateRegistration(name, email, password, confirmPassword)?.let {
            _message.value = it
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _message.value = ""

            try {
                supabase.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                }

                Log.d(TAG, "Registration succeeded")
                _message.value = "Account created successfully!"
                onSuccess()

            } catch (e: Exception) {
                Log.e(TAG, "Registration failed", e)
                _message.value = e.message ?: "Registration failed."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        InputValidator.validateLogin(email, password)?.let {
            _message.value = it
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _message.value = ""

            try {
                supabase.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }

                Log.d(TAG, "Email login succeeded")
                _message.value = "Login successful!"
                onSuccess()

            } catch (e: Exception) {
                Log.e(TAG, "Login failed", e)
                _message.value = e.message ?: "Login failed."
            } finally {
                _isLoading.value = false
            }
        }
    }

    /** Single Sign-On: Google account -> ID token -> Supabase session. */
    fun loginWithGoogle(activityContext: Context, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _message.value = ""
            try {
                val token = GoogleSignInHelper.getIdToken(activityContext)
                if (token == null) {
                    _message.value = "Google sign-in was cancelled or is not configured."
                    return@launch
                }
                supabase.auth.signInWith(IDToken) {
                    idToken = token
                    provider = Google
                }
                Log.d(TAG, "Google SSO succeeded")
                onSuccess()
            } catch (e: Exception) {
                Log.e(TAG, "Google SSO failed", e)
                _message.value = e.message ?: "Google sign-in failed."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout(
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _message.value = ""

            try {
                supabase.auth.signOut()
                _message.value = "Logged out successfully."
                onSuccess()
            } catch (e: Exception) {
                _message.value = e.message ?: "Logout failed."
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun clearMessage() {
        _message.value = ""
    }
}