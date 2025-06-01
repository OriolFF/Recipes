package com.uriolus.recipes.feature.recipe_list.data.source

import com.uriolus.recipes.core.data.local.RecipesDatabase
import com.uriolus.recipes.feature.links_list.data.source.local.RecipeLinkEntity
import com.uriolus.recipes.feature.recipe_list.data.source.local.model.toEntity
import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomRecipeDataSource @Inject constructor(
    private val database: RecipesDatabase
) : RecipeDataSource {
    
    override suspend fun getRecipes(): List<Recipe> {
        return database.recipeLinkDao.getAllLinks().map { entities ->
            entities.map { entity ->
                Recipe(
                    id = entity.id.toString(), 
                    name = entity.title,
                    description = entity.description,
                    imageUrl = entity.thumbnailUrl.ifEmpty { DEFAULT_IMAGE_URL },
                    ingredients = emptyList(), 
                    instructions = emptyList(), 
                    sourceUrl = entity.url
                )
            }
        }.first()
    }

    override suspend fun saveRecipes(recipes: List<Recipe>) {
        database.recipeLinkDao.deleteAll() // Clear existing recipes
        val entities = recipes.map { recipe ->
            RecipeLinkEntity(
                url = recipe.sourceUrl ?: "", // Use sourceUrl from Recipe domain model
                title = recipe.name,
                description = recipe.description,
                thumbnailUrl = recipe.imageUrl,
                createdAt = System.currentTimeMillis()
            )
        }
        entities.forEach { entity ->
            database.recipeLinkDao.insertLink(entity)
        }
    }

    override suspend fun saveRecipe(recipe: Recipe) {
        val recipeEntity = recipe.toEntity() // Convert domain Recipe to RecipeEntity
        database.recipeDao().insertRecipe(recipeEntity) // Use RecipeDao
    }

    override suspend fun extractRecipeFromUrl(url: String): Recipe? {
        throw UnsupportedOperationException("Extracting from URL is not supported by RoomRecipeDataSource")
    }

    private fun extractIngredientsAndInstructions(description: String): Pair<List<String>, List<String>> {
        val ingredients = mutableListOf<String>()
        val instructions = mutableListOf<String>()
        
        // Split the description into sections
        val sections = description.split("\n\n")
        
        // Look for ingredients and instructions sections
        for (section in sections) {
            when {
                section.startsWith("Ingredients:", ignoreCase = true) -> {
                    // Extract ingredients
                    val lines = section.lines().drop(1) // Skip the "Ingredients:" header
                    for (line in lines) {
                        val trimmedLine = line.trim()
                        if (trimmedLine.isNotEmpty()) {
                            // Handle bullet points or numbered lists
                            val ingredient = when {
                                trimmedLine.startsWith("- ") -> trimmedLine.substring(2)
                                trimmedLine.startsWith("• ") -> trimmedLine.substring(2)
                                else -> trimmedLine
                            }
                            ingredients.add(ingredient)
                        }
                    }
                }
                section.startsWith("Instructions:", ignoreCase = true) -> {
                    // Extract instructions
                    val lines = section.lines().drop(1) // Skip the "Instructions:" header
                    for (line in lines) {
                        val trimmedLine = line.trim()
                        if (trimmedLine.isNotEmpty()) {
                            // Handle numbered lists
                            val instruction = when {
                                trimmedLine.matches(Regex("^\\d+\\.\\s+.*$")) -> {
                                    val dotIndex = trimmedLine.indexOf('.')
                                    trimmedLine.substring(dotIndex + 1).trim()
                                }
                                else -> trimmedLine
                            }
                            instructions.add(instruction)
                        }
                    }
                }
            }
        }
        
        // If no structured sections were found, try the original approach
        if (ingredients.isEmpty() && instructions.isEmpty()) {
            val lines = description.split("\n", "\r\n")
            
            var currentSection = ""
            
            for (line in lines) {
                val trimmedLine = line.trim()
                
                when {
                    trimmedLine.isEmpty() -> continue
                    
                    trimmedLine.contains("ingredient", ignoreCase = true) || 
                    trimmedLine.contains("you need", ignoreCase = true) ||
                    trimmedLine.contains("you'll need", ignoreCase = true) -> {
                        currentSection = "ingredients"
                    }
                    
                    trimmedLine.contains("instruction", ignoreCase = true) || 
                    trimmedLine.contains("direction", ignoreCase = true) ||
                    trimmedLine.contains("step", ignoreCase = true) ||
                    trimmedLine.contains("method", ignoreCase = true) -> {
                        currentSection = "instructions"
                    }
                    
                    trimmedLine.startsWith("-") || trimmedLine.startsWith("•") || 
                    (trimmedLine.length > 2 && trimmedLine[0].isDigit() && trimmedLine[1] == '.') -> {
                        val content = trimmedLine.substring(trimmedLine.indexOfFirst { it == ' ' } + 1)
                        if (currentSection == "ingredients") {
                            ingredients.add(content)
                        } else if (currentSection == "instructions") {
                            instructions.add(content)
                        }
                    }
                }
            }
        }
        
        // If we still couldn't extract any structured data, provide some default placeholders
        if (ingredients.isEmpty()) {
            ingredients.add("No ingredients information available")
        }
        
        if (instructions.isEmpty()) {
            instructions.add("No instructions available. Check the original recipe at the link.")
        }
        
        return Pair(ingredients, instructions)
    }
    
    companion object {
        private const val DEFAULT_IMAGE_URL = "https://images.unsplash.com/photo-1495521821757-a1efb6729352?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=800&q=80"
    }
}
