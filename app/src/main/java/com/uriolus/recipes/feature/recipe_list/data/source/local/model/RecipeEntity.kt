package com.uriolus.recipes.feature.recipe_list.data.source.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.uriolus.recipes.core.data.local.converters.ListStringTypeConverter

@Entity(tableName = "recipes")
@TypeConverters(ListStringTypeConverter::class)
data class RecipeEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val sourceUrl: String?,
    val ingredients: List<String> = emptyList(),
    val instructions: List<String> = emptyList()
)
