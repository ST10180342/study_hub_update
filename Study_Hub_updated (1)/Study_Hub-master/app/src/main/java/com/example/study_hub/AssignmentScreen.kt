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
fun AssignmentScreen(
    viewModel: AssignmentViewModel,
    onBack: () -> Unit
) {

    var title by remember {
        mutableStateOf("")
    }

    var subject by remember {
        mutableStateOf("")
    }

    var dueDate by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    val assignments by viewModel.assignments.collectAsState()
    val message by viewModel.message.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            text = "Assignment Manager",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
            },
            label = {
                Text("Assignment Title")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

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

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = dueDate,
            onValueChange = {
                dueDate = it
            },
            label = {
                Text("Due Date")
            },
            placeholder = {
                Text("e.g. 30 September 2026")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
            },
            label = {
                Text("Description")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {

                viewModel.saveAssignment(
                    title = title,
                    subject = subject,
                    dueDate = dueDate,
                    description = description
                )

                title = ""
                subject = ""
                dueDate = ""
                description = ""

            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                if (isLoading) {
                    "Saving..."
                } else {
                    "Save Assignment"
                }
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        if (message.isNotBlank()) {

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(15.dp))
        }

        Text(
            text = "My Assignments",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(15.dp))

        if (assignments.isEmpty()) {

            Text("No assignments saved yet.")

        } else {

            assignments.forEach { assignment ->

                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column {

                        Text(
                            text = assignment.title,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = "Subject: ${assignment.subject}"
                        )

                        Text(
                            text = "Due: ${assignment.due_date}"
                        )

                        if (!assignment.description.isNullOrBlank()) {

                            Text(
                                text = assignment.description
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}