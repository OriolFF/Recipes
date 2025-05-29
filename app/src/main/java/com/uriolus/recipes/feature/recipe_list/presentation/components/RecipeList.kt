package com.uriolus.recipes.feature.recipe_list.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uriolus.recipes.R
import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import com.uriolus.recipes.ui.theme.RecipiesTheme
import kotlinx.coroutines.delay

@Composable
fun RecipeList(
    recipes: List<Recipe>,
    isLoading: Boolean,
    error: String?,
    onRecipeClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                LoadingState()
            }
            error != null -> {
                ErrorState(error)
            }
            recipes.isEmpty() -> {
                EmptyState()
            }
            else -> {
                RecipeListContent(
                    recipes = recipes,
                    onRecipeClick = onRecipeClick
                )
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp)
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Loading delicious recipes...",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ErrorState(error: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(32.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_error),
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Oops! Something went wrong",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = error,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun EmptyState() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(32.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_no_food),
            contentDescription = "No recipes",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No recipes found",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Try adding some new recipes or check back later",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun RecipeListContent(
    recipes: List<Recipe>,
    onRecipeClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        itemsIndexed(recipes) { index, recipe ->
            var visible by remember { mutableStateOf(false) }
            
            LaunchedEffect(key1 = recipe.id) {
                delay(index * 50L)
                visible = true
            }
            
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(durationMillis = 300)) +
                        slideInVertically(
                            animationSpec = tween(durationMillis = 300),
                            initialOffsetY = { it / 5 }
                        )
            ) {
                RecipeItem(
                    recipe = recipe,
                    onRecipeClick = onRecipeClick
                )
            }
        }
        
        // Add some space at the bottom
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeListPreview() {
    RecipiesTheme {
        RecipeList(
            recipes = listOf(
                Recipe(
                    id = "1",
                    name = "Spaghetti Carbonara",
                    description = "A classic Italian pasta dish with eggs, cheese, pancetta, and pepper.",
                    imageUrl = "https://images.unsplash.com/photo-1612874742237-6526221588e3",
                    ingredients = listOf("Spaghetti", "Eggs", "Pancetta", "Parmesan", "Black Pepper"),
                    instructions = listOf("Cook pasta", "Fry pancetta", "Mix eggs and cheese", "Combine all")
                ),
                Recipe(
                    id = "2",
                    name = "Avocado Toast",
                    description = "A simple and nutritious breakfast with mashed avocado on toasted bread.",
                    imageUrl = "https://images.unsplash.com/photo-1541519227354-08fa5d50c44d",
                    ingredients = listOf("Bread", "Avocado", "Lemon juice", "Salt", "Pepper"),
                    instructions = listOf("Toast bread", "Mash avocado", "Add seasoning", "Spread on toast")
                )
            ),
            isLoading = false,
            error = null,
            onRecipeClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeListLoadingPreview() {
    RecipiesTheme {
        RecipeList(
            recipes = emptyList(),
            isLoading = true,
            error = null,
            onRecipeClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeListErrorPreview() {
    RecipiesTheme {
        RecipeList(
            recipes = emptyList(),
            isLoading = false,
            error = "Failed to load recipes. Please check your internet connection.",
            onRecipeClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeListEmptyPreview() {
    RecipiesTheme {
        RecipeList(
            recipes = emptyList(),
            isLoading = false,
            error = null,
            onRecipeClick = {}
        )
    }
}
