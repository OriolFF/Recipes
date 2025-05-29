package com.uriolus.recipes.feature.recipe_list.data.source.remote

import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe
import kotlinx.serialization.Serializable

/**
 * Data transfer object for recipe data from the API
 */
@Serializable
data class RecipeDto(
    val id: String,
    val name: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val ingredients: List<String> = emptyList(),
    val instructions: List<String> = emptyList(),
    val sourceUrl: String? = null
)

/**
 * Extension function to convert RecipeDto to domain Recipe model
 */
fun RecipeDto.toDomain(): Recipe {
    return Recipe(
        id = id,
        name = name,
        description = description ?: "",
        imageUrl = imageUrl ?: "",
        ingredients = ingredients,
        instructions = instructions,
        sourceUrl = sourceUrl
    )
}

/**
 * Data class for the extract recipe request
 */
@Serializable
data class ExtractRecipeRequest(
    val url: String
)
