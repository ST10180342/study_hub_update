package com.example.study_hub

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import android.app.Activity
import android.widget.Toast
import androidx.compose.material3.FilterChip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current

    // Settings are loaded from (and saved to) SharedPreferences so they persist
    var notificationsEnabled by remember { mutableStateOf(SettingsStore.notificationsEnabled(context)) }
    var remindersEnabled by remember { mutableStateOf(SettingsStore.remindersEnabled(context)) }
    var darkModeEnabled by remember { mutableStateOf(SettingsStore.darkMode(context)) }
    var language by remember { mutableStateOf(SettingsStore.language(context)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = stringResource(R.string.settings),
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = stringResource(R.string.notifications),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = stringResource(R.string.enable_notifications)
            )

            Switch(
                checked = notificationsEnabled,
                onCheckedChange = {
                    notificationsEnabled = it
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.study_reminders),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "Enable study reminders"
            )

            Switch(
                checked = remindersEnabled,
                onCheckedChange = {
                    remindersEnabled = it
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.appearance),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = stringResource(R.string.dark_mode)
            )

            Switch(
                checked = darkModeEnabled,
                onCheckedChange = {
                    darkModeEnabled = it
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Multi-language: English + two South African languages
        Text(
            text = stringResource(R.string.language),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("en" to "English", "zu" to "isiZulu", "af" to "Afrikaans").forEach { (code, label) ->
                FilterChip(
                    selected = language == code,
                    onClick = { language = code },
                    label = { Text(label) }
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                val app = StudyHubApplication.instance
                SettingsStore.save(context, notificationsEnabled, remindersEnabled, darkModeEnabled, language)
                if (notificationsEnabled) app.subscribeToUpdates() else app.unsubscribeFromUpdates()
                Toast.makeText(context, context.getString(R.string.settings_saved), Toast.LENGTH_SHORT).show()
                // Recreate so the new language / theme are applied immediately
                (context as? Activity)?.recreate()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_settings))
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.back))
        }
    }
}