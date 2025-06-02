package com.uriolus.recipes.feature.recipe_list.mapper

import com.uriolus.recipes.feature.recipe_list.data.source.remote.dto.RecipeDto
import com.uriolus.recipes.feature.recipe_list.domain.model.Recipe

fun RecipeDto.toDomain(): Recipe = Recipe(
    id = this.id,
    title = this.title,
    description = this.description,
    imageUrl = this.imageUrl,
    ingredients = this.ingredients,
    steps = this.steps,
    sourceUrl = this.sourceUrl
)
