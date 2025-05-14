package com.uriolus.recipes.feature.links_list.domain.model

data class RecipeLink(
    val id: Long = 0,
    val url: String,
    val title: String,
    val description: String = "",
    val thumbnailUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
