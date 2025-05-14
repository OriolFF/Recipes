package com.uriolus.recipes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uriolus.recipes.feature.recipe_detail.presentation.screen.RecipeDetailScreen
import com.uriolus.recipes.feature.recipe_list.presentation.screen.RecipeListScreen
import com.uriolus.recipes.ui.theme.RecipiesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipiesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RecipeApp()
                }
            }
        }
    }
}

@Composable
fun RecipeApp() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "recipe_list"
    ) {
        composable(route = "recipe_list") {
            RecipeListScreen(
                onNavigateToRecipeDetail = { recipeId ->
                    navController.navigate("recipe_detail/$recipeId")
                }
            )
        }
        
        composable(
            route = "recipe_detail/{recipeId}",
            arguments = listOf(
                navArgument("recipeId") {
                    type = NavType.StringType
                }
            )
        ) {
            RecipeDetailScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}