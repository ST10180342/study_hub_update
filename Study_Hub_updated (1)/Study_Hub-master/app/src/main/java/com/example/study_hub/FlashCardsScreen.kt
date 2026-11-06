
package com.example.study_hub

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FlashcardsScreen(
    viewModel: FlashcardViewModel,
    onBack: () -> Unit
) {

    var subject by remember {
        mutableStateOf("")
    }

    var question by remember {
        mutableStateOf("")
    }

    var answer by remember {
        mutableStateOf("")
    }

    val flashcards by viewModel.flashcards.collectAsState()
    val message by viewModel.message.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Flashcards",
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
            value = answer,
            onValueChange = {
                answer = it
            },
            label = {
                Text("Answer")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {

                viewModel.saveFlashcard(
                    subject = subject,
                    question = question,
                    answer = answer
                )

                subject = ""
                question = ""
                answer = ""

            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                if (isLoading) {
                    "Saving..."
                } else {
                    "Save Flashcard"
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
            text = "My Flashcards",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(15.dp))

        if (flashcards.isEmpty()) {

            Text("No flashcards saved yet.")

        } else {

            flashcards.forEach { flashcard ->

                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        horizontalAlignment = Alignment.Start
                    ) {

                        Text(
                            text = flashcard.subject,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = "Q: ${flashcard.question}"
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = "A: ${flashcard.answer}"
                        )
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