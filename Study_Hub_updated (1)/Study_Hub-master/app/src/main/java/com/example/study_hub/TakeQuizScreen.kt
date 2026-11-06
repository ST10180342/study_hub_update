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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TakeQuizScreen(
    viewModel: QuizViewModel,
    onBack: () -> Unit
) {
    val questions by viewModel.questions.collectAsState()

    var currentQuestion by remember {
        mutableIntStateOf(0)
    }

    var selectedAnswer by remember {
        mutableStateOf("")
    }

    var score by remember {
        mutableIntStateOf(0)
    }

    var quizFinished by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        viewModel.loadQuestions()
    }

    // No questions available
    if (questions.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "No Quiz Questions",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Create some quiz questions first."
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }
        }

        return
    }

    // Quiz finished
    if (quizFinished) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Quiz Complete!",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Your score: $score / ${questions.size}",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    currentQuestion = 0
                    selectedAnswer = ""
                    score = 0
                    quizFinished = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Try Again")
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }
        }

        return
    }

    // Make sure the question index is valid
    if (currentQuestion >= questions.size) {
        currentQuestion = 0
        selectedAnswer = ""
    }

    val question = questions[currentQuestion]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Practice Quiz",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Question ${currentQuestion + 1} of ${questions.size}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "Subject: ${question.subject}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = question.question,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(25.dp))

        QuizAnswerButton(
            letter = "A",
            answer = question.option_a,
            selected = selectedAnswer == "A",
            onClick = {
                selectedAnswer = "A"
            }
        )

        QuizAnswerButton(
            letter = "B",
            answer = question.option_b,
            selected = selectedAnswer == "B",
            onClick = {
                selectedAnswer = "B"
            }
        )

        QuizAnswerButton(
            letter = "C",
            answer = question.option_c,
            selected = selectedAnswer == "C",
            onClick = {
                selectedAnswer = "C"
            }
        )

        QuizAnswerButton(
            letter = "D",
            answer = question.option_d,
            selected = selectedAnswer == "D",
            onClick = {
                selectedAnswer = "D"
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (selectedAnswer.isNotEmpty()) {

                    if (
                        selectedAnswer.equals(
                            question.correct_answer.trim(),
                            ignoreCase = true
                        )
                    ) {
                        score++
                    }

                    if (currentQuestion < questions.lastIndex) {
                        currentQuestion++
                        selectedAnswer = ""
                    } else {
                        quizFinished = true
                    }
                }
            },
            enabled = selectedAnswer.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (currentQuestion < questions.lastIndex) {
                    "Next Question"
                } else {
                    "Finish Quiz"
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

@Composable
fun QuizAnswerButton(
    letter: String,
    answer: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )

        Spacer(modifier = Modifier.padding(horizontal = 4.dp))

        Text(
            text = "$letter. $answer",
            modifier = Modifier.fillMaxWidth()
        )
    }
}