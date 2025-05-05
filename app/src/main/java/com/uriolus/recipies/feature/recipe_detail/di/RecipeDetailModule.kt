package com.uriolus.recipies.feature.recipe_detail.di

import com.uriolus.recipies.feature.recipe_detail.domain.use_case.GetRecipeByIdUseCase
import com.uriolus.recipies.feature.recipe_list.domain.repository.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecipeDetailModule {
    
    @Provides
    @Singleton
    fun provideGetRecipeByIdUseCase(repository: RecipeRepository): GetRecipeByIdUseCase {
        return GetRecipeByIdUseCase(repository)
    }
}
