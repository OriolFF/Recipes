package com.uriolus.recipes.core.di

import com.uriolus.recipes.core.data.local.RecipesDatabase
import com.uriolus.recipes.feature.recipe_list.data.source.local.dao.RecipeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {


    @Provides
    fun provideRecipeDao(database: RecipesDatabase): RecipeDao {
        return database.recipeDao()
    }

}
