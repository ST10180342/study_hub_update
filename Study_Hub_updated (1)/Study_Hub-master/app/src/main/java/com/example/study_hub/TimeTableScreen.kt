package com.example.study_hub

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TimetableScreen(
    viewModel: TimetableViewModel,
    onBack: () -> Unit
) {

    var subject by remember {
        mutableStateOf("")
    }

    var day by remember {
        mutableStateOf("")
    }

    var startTime by remember {
        mutableStateOf("")
    }

    var endTime by remember {
        mutableStateOf("")
    }

    var room by remember {
        mutableStateOf("")
    }

    val entries by viewModel.entries.collectAsState()
    val message by viewModel.message.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            text = "Timetable",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        OutlinedTextField(
            value = subject,
            onValueChange = {
                subject = it
            },
            label = {
                Text("Subject")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        OutlinedTextField(
            value = day,
            onValueChange = {
                day = it
            },
            label = {
                Text("Day")
            },
            placeholder = {
                Text("e.g. Monday")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        OutlinedTextField(
            value = startTime,
            onValueChange = {
                startTime = it
            },
            label = {
                Text("Start Time")
            },
            placeholder = {
                Text("e.g. 09:00")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        OutlinedTextField(
            value = endTime,
            onValueChange = {
                endTime = it
            },
            label = {
                Text("End Time")
            },
            placeholder = {
                Text("e.g. 10:00")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        OutlinedTextField(
            value = room,
            onValueChange = {
                room = it
            },
            label = {
                Text("Room")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(15.dp)
        )

        Button(
            onClick = {

                viewModel.saveTimetableEntry(
                    subject = subject,
                    day = day,
                    startTime = startTime,
                    endTime = endTime,
                    room = room
                )

                subject = ""
                day = ""
                startTime = ""
                endTime = ""
                room = ""

            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                if (isLoading) {
                    "Saving..."
                } else {
                    "Save Timetable Entry"
                }
            )
        }

        Spacer(
            modifier = Modifier.height(15.dp)
        )

        if (message.isNotBlank()) {

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier = Modifier.height(15.dp)
            )
        }

        Text(
            text = "My Timetable",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(
            modifier = Modifier.height(15.dp)
        )

        if (entries.isEmpty()) {

            Text("No timetable entries saved yet.")

        } else {

            entries.forEach { entry ->

                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column {

                        Text(
                            text = entry.subject,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = "Day: ${entry.day}"
                        )

                        Text(
                            text = "Time: ${entry.start_time} - ${entry.end_time}"
                        )

                        if (!entry.room.isNullOrBlank()) {

                            Text(
                                text = "Room: ${entry.room}"
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}