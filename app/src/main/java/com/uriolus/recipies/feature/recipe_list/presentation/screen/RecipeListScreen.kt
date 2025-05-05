package com.uriolus.recipies.feature.recipe_list.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.uriolus.recipies.feature.recipe_list.presentation.components.RecipeList
import com.uriolus.recipies.feature.recipe_list.presentation.state.RecipeListAction
import com.uriolus.recipies.feature.recipe_list.presentation.viewmodel.RecipeListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(
    onNavigateToRecipeDetail: (String) -> Unit,
    viewModel: RecipeListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipes") },
                actions = {
                    IconButton(onClick = { /* TODO: Implement search */ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search recipes"
                        )
                    }
                }
            )
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
                onRecipeClick = { recipeId ->
                    viewModel.handleAction(RecipeListAction.RecipeClicked(recipeId))
                    onNavigateToRecipeDetail(recipeId)
                }
            )
        }
    }
}
