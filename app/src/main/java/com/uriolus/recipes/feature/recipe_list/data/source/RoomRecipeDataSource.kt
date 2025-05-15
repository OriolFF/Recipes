package com.uriolus.recipes.feature.recipe_list.data.source

import com.uriolus.recipes.feature.links_list.data.source.local.RecipesDatabase
import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomRecipeDataSource @Inject constructor(
    private val database: RecipesDatabase
) : RecipeDataSource {
    
    override fun getRecipes(): Flow<List<Recipe>> {
        return database.recipeLinkDao.getAllLinks().map { entities ->
            entities.map { entity ->
                // Extract potential ingredients and instructions from the description
                val (ingredients, instructions) = extractIngredientsAndInstructions(entity.description)
                
                Recipe(
                    id = entity.id.toString(),
                    title = entity.title,
                    description = entity.description,
                    imageUrl = entity.thumbnailUrl.ifEmpty { DEFAULT_IMAGE_URL },
                    ingredients = ingredients,
                    instructions = instructions
                )
            }
        }
    }
    
    private fun extractIngredientsAndInstructions(description: String): Pair<List<String>, List<String>> {
        // This is a simple implementation that tries to extract ingredients and instructions
        // from the description based on common patterns
        val lines = description.split("\n", "\r\n")
        
        val ingredients = mutableListOf<String>()
        val instructions = mutableListOf<String>()
        
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
        
        // If we couldn't extract any structured data, provide some default placeholders
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
