package com.uriolus.recipies.feature.recipe_detail.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.uriolus.recipies.feature.recipe_list.domain.model.Recipe
import com.uriolus.recipies.ui.theme.RecipiesTheme

@Composable
fun RecipeHeader(
    recipe: Recipe,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(MaterialTheme.shapes.medium)
    ) {
        AsyncImage(
            model = recipe.imageUrl,
            contentDescription = recipe.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
        )
        
        // Gradient overlay for better text visibility
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        ),
                        startY = 0f,
                        endY = 700f
                    )
                )
        )
        
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Text(
                text = recipe.description,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeHeaderPreview() {
    RecipiesTheme {
        RecipeHeader(
            recipe = Recipe(
                id = "1",
                title = "Spaghetti Carbonara",
                description = "A classic Italian pasta dish with eggs, cheese, pancetta, and pepper.",
                imageUrl = "https://images.unsplash.com/photo-1612874742237-6526221588e3",
                ingredients = listOf("Spaghetti", "Eggs", "Pancetta", "Parmesan", "Black Pepper"),
                instructions = listOf("Cook pasta", "Fry pancetta", "Mix eggs and cheese", "Combine all")
            )
        )
    }
}
