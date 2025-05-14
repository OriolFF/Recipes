package com.uriolus.recipes.feature.recipe_list.domain.model

data class Recipe(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val ingredients: List<String>,
    val instructions: List<String>
)
