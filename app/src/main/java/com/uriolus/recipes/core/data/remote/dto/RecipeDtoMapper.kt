package com.uriolus.recipes.core.data.remote.dto

import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe


fun RecipeDto.toDomain(): Recipe {
    return Recipe(
        id = this.id.toString(),
        name = this.name, // DTO 'name' maps to domain 'title'
        description = this.description, // Using DTO 'name' for description as well, clarify if different source
        imageUrl = this.imageUrl?:"" , // Provide a default if null
        ingredients = this.ingredients,
        instructions = this.instructions
    )
}

