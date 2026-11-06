package com.example.study_hub

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

/**
 * Persists the user's settings (notifications, reminders, dark mode, language)
 * in SharedPreferences so they survive app restarts.
 */
object SettingsStore {
    private const val PREFS = "study_hub_settings"

    private fun prefs(c: Context) = c.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun notificationsEnabled(c: Context) = prefs(c).getBoolean("notifications", true)
    fun remindersEnabled(c: Context) = prefs(c).getBoolean("reminders", true)
    fun darkMode(c: Context) = prefs(c).getBoolean("dark", false)
    /** "en", "zu" (isiZulu) or "af" (Afrikaans) */
    fun language(c: Context) = prefs(c).getString("lang", "en") ?: "en"

    fun save(c: Context, notifications: Boolean, reminders: Boolean, dark: Boolean, lang: String) {
        prefs(c).edit()
            .putBoolean("notifications", notifications)
            .putBoolean("reminders", reminders)
            .putBoolean("dark", dark)
            .putString("lang", lang)
            .apply()
    }

    /** Wraps a context so that string resources resolve in the saved language. */
    fun wrapWithLocale(base: Context): Context {
        val locale = Locale(language(base))
        Locale.setDefault(locale)
        val config = Configuration(base.resources.configuration)
        config.setLocale(locale)
        return base.createConfigurationContext(config)
    }
}
