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
fun ExamScreen(
    viewModel: ExamViewModel,
    onBack: () -> Unit
) {

    var subject by remember {
        mutableStateOf("")
    }

    var examDate by remember {
        mutableStateOf("")
    }

    var examTime by remember {
        mutableStateOf("")
    }

    var venue by remember {
        mutableStateOf("")
    }

    var notes by remember {
        mutableStateOf("")
    }

    val exams by viewModel.exams.collectAsState()
    val message by viewModel.message.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            text = "Exam Planner",
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
            value = examDate,
            onValueChange = {
                examDate = it
            },
            label = {
                Text("Exam Date")
            },
            placeholder = {
                Text("e.g. 15 October 2026")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        OutlinedTextField(
            value = examTime,
            onValueChange = {
                examTime = it
            },
            label = {
                Text("Exam Time")
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
            value = venue,
            onValueChange = {
                venue = it
            },
            label = {
                Text("Venue")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        OutlinedTextField(
            value = notes,
            onValueChange = {
                notes = it
            },
            label = {
                Text("Notes")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(15.dp)
        )

        Button(
            onClick = {

                viewModel.saveExam(
                    subject = subject,
                    examDate = examDate,
                    examTime = examTime,
                    venue = venue,
                    notes = notes
                )

                subject = ""
                examDate = ""
                examTime = ""
                venue = ""
                notes = ""

            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                if (isLoading) {
                    "Saving..."
                } else {
                    "Save Exam"
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
            text = "My Exams",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(
            modifier = Modifier.height(15.dp)
        )

        if (exams.isEmpty()) {

            Text("No exams saved yet.")

        } else {

            exams.forEach { exam ->

                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column {

                        Text(
                            text = exam.subject,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = "Date: ${exam.exam_date}"
                        )

                        Text(
                            text = "Time: ${exam.exam_time}"
                        )

                        if (!exam.venue.isNullOrBlank()) {

                            Text(
                                text = "Venue: ${exam.venue}"
                            )
                        }

                        if (!exam.notes.isNullOrBlank()) {

                            Text(
                                text = "Notes: ${exam.notes}"
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