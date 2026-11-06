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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun FocusTimerWithSessionsScreen(
    viewModel: StudySessionViewModel,
    onBack: () -> Unit
) {

    var selectedMinutes by remember {
        mutableStateOf(25)
    }

    var timeLeft by remember {
        mutableLongStateOf(25 * 60L)
    }

    var isRunning by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(isRunning) {

        while (isRunning && timeLeft > 0) {

            delay(1000L)

            timeLeft--

            if (timeLeft <= 0) {

                isRunning = false

                // Save the completed study session
                viewModel.saveCompletedSession(selectedMinutes)
            }
        }
    }

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Focus Timer",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Stay focused and make the most of your study time."
        )

        Spacer(
            modifier = Modifier.height(40.dp)
        )

        Text(
            text = String.format(
                "%02d:%02d",
                minutes,
                seconds
            ),
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        Text(
            text = "Study Duration",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            OutlinedButton(
                onClick = {
                    if (!isRunning) {
                        selectedMinutes = 15
                        timeLeft = 15 * 60L
                    }
                }
            ) {
                Text("15 min")
            }

            OutlinedButton(
                onClick = {
                    if (!isRunning) {
                        selectedMinutes = 25
                        timeLeft = 25 * 60L
                    }
                }
            ) {
                Text("25 min")
            }

            OutlinedButton(
                onClick = {
                    if (!isRunning) {
                        selectedMinutes = 45
                        timeLeft = 45 * 60L
                    }
                }
            ) {
                Text("45 min")
            }
        }

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        Button(
            onClick = {
                isRunning = !isRunning
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (isRunning) {
                    "Pause"
                } else {
                    "Start Focus"
                }
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        OutlinedButton(
            onClick = {
                isRunning = false
                timeLeft = selectedMinutes * 60L
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reset")
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}