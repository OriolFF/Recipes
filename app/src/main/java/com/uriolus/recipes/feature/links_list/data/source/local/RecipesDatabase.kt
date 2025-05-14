package com.uriolus.recipes.feature.links_list.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [RecipeLinkEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RecipesDatabase : RoomDatabase() {
    abstract val recipeLinkDao: RecipeLinkDao
    
    companion object {
        const val DATABASE_NAME = "recipes_db"
    }
}
