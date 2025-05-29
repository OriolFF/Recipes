package com.uriolus.recipes.feature.recipe_list.data.local.model

import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe


fun RecipeEntity.toDomain(): Recipe {
    return Recipe(
        id = this.id,
        name = this.title,
        description = this.description,
        imageUrl = this.imageUrl,
        ingredients = this.ingredients,
        instructions = this.instructions,
        sourceUrl = this.sourceUrl
    )
}

fun List<RecipeEntity>.toDomain(): List<Recipe> {
    return this.map { it.toDomain() }
}

fun Recipe.toEntity(): RecipeEntity {
    return RecipeEntity(
        id = this.id,
        title = this.name,
        description = this.description,
        imageUrl = this.imageUrl ?: "",
        ingredients = this.ingredients,
        instructions = this.instructions,
        sourceUrl = this.sourceUrl
    )
}

fun List<Recipe>.toEntity(): List<RecipeEntity> {
    return this.map { it.toEntity() }
}
