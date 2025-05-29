package com.uriolus.recipes.feature.recipe_list.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.uriolus.recipes.NavRoutes
import com.uriolus.recipes.R
import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import com.uriolus.recipes.feature.recipe_list.presentation.components.RecipeList
import com.uriolus.recipes.feature.recipe_list.presentation.state.RecipeListAction
import com.uriolus.recipes.feature.recipe_list.presentation.state.RecipeListState
import com.uriolus.recipes.feature.recipe_list.presentation.viewmodel.RecipeListViewModel
import com.uriolus.recipes.ui.theme.RecipiesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(
    navController: NavController,
    onNavigateToRecipeDetail: (String) -> Unit,
    onNavigateToLinksList: () -> Unit,
    viewModel: RecipeListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.logoutRequested) {
        if (state.logoutRequested) {
            navController.navigate(NavRoutes.LOGIN) {
                popUpTo(NavRoutes.RECIPE_LIST) { inclusive = true } // Or popUpTo(0) to clear all
            }
            viewModel.onLogoutHandled() // Reset the flag
        }
    }

    LaunchedEffect(state.authenticationErrorOccurred) {
        if (state.authenticationErrorOccurred) {
            navController.navigate(NavRoutes.LOGIN) {
                popUpTo(NavRoutes.RECIPE_LIST) { inclusive = true }
            }
            viewModel.onAuthenticationErrorHandled() // Reset the flag
        }
    }
    
    RecipeListScreenContent(
        state = state,
        onRecipeClick = { recipeId ->
            viewModel.handleAction(RecipeListAction.RecipeClicked(recipeId))
            onNavigateToRecipeDetail(recipeId)
        },
        onSearchClick = { /* TODO: Implement search */ },
        onAddRecipeClick = { /* TODO: Implement add recipe */ },
        onLinksListClick = onNavigateToLinksList,
        onLogoutClick = {
            viewModel.handleAction(RecipeListAction.LogoutClicked)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreenContent(
    state: RecipeListState,
    onRecipeClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onAddRecipeClick: () -> Unit,
    onLinksListClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "My Recipes",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                actions = {
                    IconButton(onClick = onLinksListClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_link),
                            contentDescription = "Recipe Links",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search recipes",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onLogoutClick) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = stringResource(id = R.string.logout_button_description),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddRecipeClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add recipe"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            RecipeList(
                recipes = state.recipes,
                isLoading = state.isLoading,
                error = state.error,
                onRecipeClick = onRecipeClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeListScreenPreview() {
    RecipiesTheme {
        RecipeListScreenContent(
            state = RecipeListState(
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
                error = null
            ),
            onRecipeClick = {},
            onSearchClick = {},
            onAddRecipeClick = {},
            onLinksListClick = {},
            onLogoutClick = {}
        )
    }
}
