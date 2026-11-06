

package com.example.study_hub

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
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
fun FocusScreen(
    onFocusTimer: () -> Unit
) {

    val cream = Color(0xFFFBFBFB)
    val blushPink = Color(0xFFF4D0D0)
    val taupe = Color(0xFFB7A09A)
    val charcoal = Color(0xFF3A3A3A)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cream)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Focus",
                style = MaterialTheme.typography.headlineLarge,
                color = charcoal
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Create focused study sessions and stay productive.",
                style = MaterialTheme.typography.bodyLarge,
                color = taupe
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = blushPink
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = "Focus Timer",
                    tint = charcoal,
                    modifier = Modifier.height(55.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Focus Timer",
                    style = MaterialTheme.typography.headlineSmall,
                    color = charcoal
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Choose a study session and focus without distractions.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = charcoal
                )

                Spacer(modifier = Modifier.height(22.dp))

                Button(
                    onClick = onFocusTimer,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Start Focus Session")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Study tip",
            style = MaterialTheme.typography.titleLarge,
            color = charcoal
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Choose a realistic session length and take a short break when your session is complete.",
            style = MaterialTheme.typography.bodyMedium,
            color = taupe
        )
    }
}