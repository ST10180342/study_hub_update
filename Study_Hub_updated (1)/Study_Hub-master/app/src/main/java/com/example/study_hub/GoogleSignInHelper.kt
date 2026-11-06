package com.example.study_hub

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

/**
 * Single Sign-On: shows the Google account picker (Credential Manager) and returns
 * a Google ID token, which AuthViewModel exchanges for a Supabase session.
 * Ref: developer.android.com/identity/sign-in/credential-manager-siwg
 */
object GoogleSignInHelper {

    /** @return Google ID token, or null if cancelled/unavailable. */
    suspend fun getIdToken(activityContext: Context): String? {
        if (BuildConfig.GOOGLE_WEB_CLIENT_ID.isBlank()) {
            Log.e(StudyHubApplication.TAG, "google.web.client.id missing in local.properties")
            return null
        }
        val option = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .build()
        val request = GetCredentialRequest.Builder().addCredentialOption(option).build()
        val credential = CredentialManager.create(activityContext)
            .getCredential(activityContext, request).credential
        return if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            GoogleIdTokenCredential.createFrom(credential.data).idToken
        } else null
    }
}
