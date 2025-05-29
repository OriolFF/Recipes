package com.uriolus.recipes.feature.recipe_list.domain.model

data class Recipe(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val sourceUrl: String? = null
)