package com.uriolus.recipies.feature.recipe_detail.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uriolus.recipies.ui.theme.RecipiesTheme

@Composable
fun InstructionsList(
    instructions: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Instructions",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        instructions.forEachIndexed { index, instruction ->
            InstructionItem(
                stepNumber = index + 1,
                instruction = instruction,
                modifier = Modifier.padding(bottom = if (index < instructions.size - 1) 16.dp else 0.dp)
            )
        }
    }
}

@Composable
fun InstructionItem(
    stepNumber: Int,
    instruction: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(16.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            ) {
                Text(
                    text = stepNumber.toString(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(4.dp)
                )
            }
            
            Text(
                text = instruction,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InstructionsListPreview() {
    RecipiesTheme {
        InstructionsList(
            instructions = listOf(
                "Cook the spaghetti in salted water according to package instructions.",
                "While pasta cooks, fry the pancetta until crispy.",
                "In a bowl, mix eggs, cheese, and pepper.",
                "Drain pasta, reserving some cooking water.",
                "Mix pasta with pancetta, then quickly stir in egg mixture off the heat.",
                "Add a splash of pasta water to create a creamy sauce.",
                "Serve immediately with extra cheese and pepper."
            )
        )
    }
}
