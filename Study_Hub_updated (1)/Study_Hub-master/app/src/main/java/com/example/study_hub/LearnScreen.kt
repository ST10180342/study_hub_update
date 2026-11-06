package com.example.study_hub

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LearnScreen(
    onFlashcards: () -> Unit,
    onQuiz: () -> Unit,
    onTakeQuiz: () -> Unit
) {

    val cream = Color(0xFFFBFBFB)
    val blushPink = Color(0xFFF4D0D0)
    val taupe = Color(0xFFB7A09A)
    val charcoal = Color(0xFF3A3A3A)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(cream)
            .padding(20.dp)
    ) {

        item {

            Text(
                text = "Learn",
                style = MaterialTheme.typography.headlineLarge,
                color = charcoal
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Build your knowledge and practise what you learn.",
                style = MaterialTheme.typography.bodyLarge,
                color = taupe
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        item {

            LearnFeatureCard(
                title = "Flashcards",
                description = "Create and review flashcards to strengthen your memory.",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Style,
                        contentDescription = "Flashcards",
                        modifier = Modifier.size(30.dp)
                    )
                },
                backgroundColor = blushPink,
                buttonText = "Open Flashcards",
                onClick = onFlashcards
            )

            Spacer(modifier = Modifier.height(14.dp))
        }

        item {

            LearnFeatureCard(
                title = "Create Quiz",
                description = "Create your own questions and build a personalised quiz.",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Quiz,
                        contentDescription = "Create Quiz",
                        modifier = Modifier.size(30.dp)
                    )
                },
                backgroundColor = Color.White,
                buttonText = "Create Quiz",
                onClick = onQuiz
            )

            Spacer(modifier = Modifier.height(14.dp))
        }

        item {

            LearnFeatureCard(
                title = "Practice Quiz",
                description = "Test yourself and see how much you remember.",
                icon = {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Practice Quiz",
                        modifier = Modifier.size(30.dp)
                    )
                },
                backgroundColor = Color.White,
                buttonText = "Start Practice",
                onClick = onTakeQuiz
            )
        }
    }
}

@Composable
fun LearnFeatureCard(
    title: String,
    description: String,
    icon: @Composable () -> Unit,
    backgroundColor: Color,
    buttonText: String,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                icon()

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0XFF3A3A3A)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6F6F6F)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(buttonText)
            }
        }
    }
}