package com.example.study_hub

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    onBack: () -> Unit
) {

    var subject by remember {
        mutableStateOf("")
    }

    var question by remember {
        mutableStateOf("")
    }

    var optionA by remember {
        mutableStateOf("")
    }

    var optionB by remember {
        mutableStateOf("")
    }

    var optionC by remember {
        mutableStateOf("")
    }

    var optionD by remember {
        mutableStateOf("")
    }

    var correctAnswer by remember {
        mutableStateOf("")
    }

    val message by viewModel.message.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.clearMessage()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "Create Quiz Question",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

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
            value = question,
            onValueChange = {
                question = it
            },
            label = {
                Text("Question")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = optionA,
            onValueChange = {
                optionA = it
            },
            label = {
                Text("Option A")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = optionB,
            onValueChange = {
                optionB = it
            },
            label = {
                Text("Option B")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = optionC,
            onValueChange = {
                optionC = it
            },
            label = {
                Text("Option C")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = optionD,
            onValueChange = {
                optionD = it
            },
            label = {
                Text("Option D")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = correctAnswer,
            onValueChange = {
                correctAnswer = it
            },
            label = {
                Text("Correct Answer (A, B, C or D)")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        if (message.isNotEmpty()) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        Button(
            onClick = {
                viewModel.saveQuestion(
                    subject = subject,
                    question = question,
                    optionA = optionA,
                    optionB = optionB,
                    optionC = optionC,
                    optionD = optionD,
                    correctAnswer = correctAnswer.uppercase()
                )
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isLoading) {
                    "Saving..."
                } else {
                    "Save Quiz Question"
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}