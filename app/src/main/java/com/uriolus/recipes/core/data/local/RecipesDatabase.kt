package com.uriolus.recipes.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.uriolus.recipes.feature.links_list.data.source.local.RecipeLinkDao
import com.uriolus.recipes.feature.links_list.data.source.local.RecipeLinkEntity
import com.uriolus.recipes.feature.recipe_list.data.local.dao.RecipeDao
import com.uriolus.recipes.feature.recipe_list.data.local.model.RecipeEntity

@Database(
    entities = [RecipeLinkEntity::class, RecipeEntity::class],
    version = 1, // If you've changed schema, you might need to increment this and add a migration
    exportSchema = false
)
abstract class RecipesDatabase : RoomDatabase() {
    abstract val recipeLinkDao: RecipeLinkDao
    abstract fun recipeDao(): RecipeDao
    
    companion object {
        const val DATABASE_NAME = "recipes_db"
    }
}
