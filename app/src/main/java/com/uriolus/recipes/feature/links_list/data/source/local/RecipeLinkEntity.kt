package com.uriolus.recipes.feature.links_list.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uriolus.recipes.feature.links_list.domain.model.RecipeLink

@Entity(tableName = "recipe_links")
data class RecipeLinkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val url: String,
    val title: String,
    val description: String,
    val thumbnailUrl: String,
    val createdAt: Long
) {
    fun toRecipeLink(): RecipeLink {
        return RecipeLink(
            id = id,
            url = url,
            title = title,
            description = description,
            thumbnailUrl = thumbnailUrl,
            createdAt = createdAt
        )
    }

    companion object {
        fun fromRecipeLink(recipeLink: RecipeLink): RecipeLinkEntity {
            return RecipeLinkEntity(
                id = recipeLink.id,
                url = recipeLink.url,
                title = recipeLink.title,
                description = recipeLink.description,
                thumbnailUrl = recipeLink.thumbnailUrl,
                createdAt = recipeLink.createdAt
            )
        }
    }
}
