package com.uriolus.recipes.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String,
    @SerialName("ingredients")
    val ingredients: List<String> = emptyList(),
    @SerialName("instructions")
    val instructions: List<String> = emptyList(),
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("source_url")
    val sourceUrl: String? = null
)
