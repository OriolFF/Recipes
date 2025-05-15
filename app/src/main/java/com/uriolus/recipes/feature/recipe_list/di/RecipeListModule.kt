package com.uriolus.recipes.feature.recipe_list.di

import com.uriolus.recipes.feature.recipe_list.data.repository.RecipeRepositoryImpl
import com.uriolus.recipes.feature.recipe_list.data.source.RecipeDataSource
import com.uriolus.recipes.feature.recipe_list.data.source.RoomRecipeDataSource
import com.uriolus.recipes.feature.recipe_list.domain.repository.RecipeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RecipeListModule {

    @Binds
    @Singleton
    abstract fun bindRecipeRepository(
        recipeRepositoryImpl: RecipeRepositoryImpl
    ): RecipeRepository

    @Binds
    @Singleton
    abstract fun bindRecipeDataSource(
        roomRecipeDataSource: RoomRecipeDataSource
    ): RecipeDataSource
}
