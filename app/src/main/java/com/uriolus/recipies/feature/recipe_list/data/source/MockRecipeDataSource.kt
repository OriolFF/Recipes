package com.uriolus.recipies.feature.recipe_list.data.source

import com.uriolus.recipies.feature.recipe_list.domain.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MockRecipeDataSource @Inject constructor() : RecipeDataSource {
    
    override fun getRecipes(): Flow<List<Recipe>> = flow {
        emit(mockRecipes)
    }
    
    companion object {
        private val mockRecipes = listOf(
            Recipe(
                id = "1",
                title = "Spaghetti Carbonara",
                description = "A classic Italian pasta dish with eggs, cheese, pancetta, and pepper.",
                imageUrl = "https://images.unsplash.com/photo-1612874742237-6526221588e3?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8Y2FyYm9uYXJhfGVufDB8fDB8fHww&auto=format&fit=crop&w=500&q=60",
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
            Recipe(
                id = "2",
                title = "Chicken Tikka Masala",
                description = "A flavorful Indian curry dish with marinated chicken in a creamy tomato sauce.",
                imageUrl = "https://images.unsplash.com/photo-1565557623262-b51c2513a641?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8dGlra2ElMjBtYXNhbGF8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&w=500&q=60",
                ingredients = listOf(
                    "800g chicken breast, cut into chunks",
                    "2 cups plain yogurt",
                    "3 tbsp lemon juice",
                    "4 tsp ground cumin",
                    "4 tsp paprika",
                    "2 tsp cinnamon",
                    "2 tsp black pepper",
                    "2 tbsp ginger, minced",
                    "6 cloves garlic, minced",
                    "2 tsp salt",
                    "2 tbsp butter",
                    "2 onions, diced",
                    "3 tbsp tomato paste",
                    "1 can tomato sauce",
                    "1 cup heavy cream",
                    "Fresh cilantro for garnish"
                ),
                instructions = listOf(
                    "Combine yogurt, lemon juice, and spices to make marinade.",
                    "Add chicken to marinade and refrigerate for at least 1 hour.",
                    "Preheat grill or oven and cook chicken until done.",
                    "In a large pan, melt butter and sauté onions until soft.",
                    "Add tomato paste, tomato sauce, and cream. Simmer for 10 minutes.",
                    "Add the cooked chicken and simmer for another 5 minutes.",
                    "Garnish with cilantro and serve with rice or naan."
                )
            ),
            Recipe(
                id = "3",
                title = "Avocado Toast",
                description = "A simple and nutritious breakfast with mashed avocado on toasted bread.",
                imageUrl = "https://images.unsplash.com/photo-1541519227354-08fa5d50c44d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8YXZvY2FkbyUyMHRvYXN0fGVufDB8fDB8fHww&auto=format&fit=crop&w=500&q=60",
                ingredients = listOf(
                    "2 slices of bread",
                    "1 ripe avocado",
                    "1 tbsp lemon juice",
                    "Salt and pepper to taste",
                    "Red pepper flakes (optional)",
                    "2 eggs (optional)"
                ),
                instructions = listOf(
                    "Toast the bread until golden and crisp.",
                    "Cut the avocado in half, remove the pit, and scoop the flesh into a bowl.",
                    "Add lemon juice, salt, and pepper, then mash with a fork.",
                    "Spread the mashed avocado on the toast.",
                    "Top with red pepper flakes if desired.",
                    "For extra protein, add a poached or fried egg on top."
                )
            )
        )
    }
}
