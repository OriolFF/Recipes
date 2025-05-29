package com.uriolus.recipes.feature.recipe_detail.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.uriolus.recipes.R
import com.uriolus.recipes.feature.recipe_detail.presentation.components.IngredientsList
import com.uriolus.recipes.feature.recipe_detail.presentation.components.InstructionsList
import com.uriolus.recipes.feature.recipe_detail.presentation.components.RecipeHeader
import com.uriolus.recipes.feature.recipe_detail.presentation.state.RecipeDetailAction
import com.uriolus.recipes.feature.recipe_detail.presentation.state.RecipeDetailState
import com.uriolus.recipes.feature.recipe_detail.presentation.viewmodel.RecipeDetailViewModel
import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import com.uriolus.recipes.ui.theme.RecipiesTheme

@Composable
fun RecipeDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: RecipeDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    RecipeDetailContent(
        state = state,
        onBackClick = {
            viewModel.handleAction(RecipeDetailAction.NavigateBack)
            onNavigateBack()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailContent(
    state: RecipeDetailState,
    onBackClick: () -> Unit
) {
    var isFavorite by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Go back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { isFavorite = !isFavorite }) {
                        Icon(
                            painter = painterResource(
                                id = if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                            ),
                            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = { /* TODO: Implement share functionality */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_share),
                            contentDescription = "Share recipe",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                state.error != null -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_error),
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = state.error,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                state.recipe != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        RecipeHeader(
                            recipe = state.recipe,
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        IngredientsList(
                            ingredients = state.recipe.ingredients,
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        InstructionsList(
                            instructions = state.recipe.instructions,
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeDetailContentPreview() {
    RecipiesTheme {
        RecipeDetailContent(
            state = RecipeDetailState(
                recipe = Recipe(
                    id = "1",
                    name = "Spaghetti Carbonara",
                    description = "A classic Italian pasta dish with eggs, cheese, pancetta, and pepper.",
                    imageUrl = "https://images.unsplash.com/photo-1612874742237-6526221588e3",
                    ingredients = listOf(
                        "400g spaghetti",
                        "200g pancetta or guanciale, diced",
                        "4 large eggs",
                        "50g pecorino cheese, grated",
                        "50g parmesan, grated",
                        "Freshly ground black pepper",
                        "Salt"
                    ),
                    instructions = listOf(
                        "Cook the spaghetti in salted water according to package instructions.",
                        "While pasta cooks, fry the pancetta until crispy.",
                        "In a bowl, mix eggs, cheese, and pepper.",
                        "Drain pasta, reserving some cooking water.",
                        "Mix pasta with pancetta, then quickly stir in egg mixture off the heat.",
                        "Add a splash of pasta water to create a creamy sauce.",
                        "Serve immediately with extra cheese and pepper."
                    )
                ),
                isLoading = false,
                error = null
            ),
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeDetailLoadingPreview() {
    RecipiesTheme {
        RecipeDetailContent(
            state = RecipeDetailState(
                recipe = null,
                isLoading = true,
                error = null
            ),
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeDetailErrorPreview() {
    RecipiesTheme {
        RecipeDetailContent(
            state = RecipeDetailState(
                recipe = null,
                isLoading = false,
                error = "Recipe not found. Please try again later."
            ),
            onBackClick = {}
        )
    }
}
