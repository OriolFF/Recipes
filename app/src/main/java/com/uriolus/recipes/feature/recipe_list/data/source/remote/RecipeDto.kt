package com.uriolus.recipes.feature.recipe_list.data.source.remote

import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import kotlinx.serialization.Serializable

/**
 * Data transfer object for recipe data from the API
 */
@Serializable
data class RecipeDto(
    val id: String,
    val title: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val ingredients: List<String> = emptyList(),
    val instructions: List<String> = emptyList(),
    val sourceUrl: String? = null
)

/**
 * Extension function to convert RecipeDto to domain Recipe model
 */
fun RecipeDto.toDomainModel(): Recipe {
    return Recipe(
        id = id,
        title = title,
        description = description ?: "",
        imageUrl = imageUrl ?: "",
        ingredients = ingredients,
        instructions = instructions
    )
}

/**
 * Data class for the extract recipe request
 */
@Serializable
data class ExtractRecipeRequest(
    val url: String
)
