package com.uriolus.recipes.feature.recipe_detail.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uriolus.recipes.ui.theme.RecipiesTheme

@Composable
fun IngredientsList(
    ingredients: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Ingredients",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        ingredients.forEachIndexed { index, ingredient ->
            IngredientItem(
                ingredient = ingredient,
                modifier = Modifier.padding(bottom = if (index < ingredients.size - 1) 8.dp else 0.dp)
            )
        }
    }
}

@Composable
fun IngredientItem(
    ingredient: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
            
            Text(
                text = ingredient,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IngredientsListPreview() {
    RecipiesTheme {
        IngredientsList(
            ingredients = listOf(
                "400g spaghetti",
                "200g pancetta or guanciale, diced",
                "4 large eggs",
                "50g pecorino cheese, grated",
                "50g parmesan, grated",
                "Freshly ground black pepper",
                "Salt"
            )
        )
    }
}
