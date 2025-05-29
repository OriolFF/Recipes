package com.uriolus.recipes.feature.recipe_list.di

import com.uriolus.recipes.core.data.preferences.TokenStorageManager
import com.uriolus.recipes.feature.recipe_list.data.source.remote.RecipeApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecipeNetworkModule {

    @Provides
    @Singleton
    fun provideRecipeApiClient(tokenStorageManager: TokenStorageManager): RecipeApiClient {
        return RecipeApiClient(tokenStorageManager)
    }

}
