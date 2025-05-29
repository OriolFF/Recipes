package com.uriolus.recipes.feature.recipe_list.di

import com.uriolus.recipes.core.data.preferences.TokenStorageManager
import com.uriolus.recipes.feature.recipe_list.data.source.RecipeDataSource
import com.uriolus.recipes.feature.recipe_list.data.source.RoomRecipeDataSource
import com.uriolus.recipes.feature.recipe_list.data.source.remote.RecipeApiClient
import com.uriolus.recipes.feature.recipe_list.data.source.remote.RemoteRecipeDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecipeNetworkModule {

    @Provides
    @Singleton
    fun provideRecipeApiClient(tokenStorageManager: TokenStorageManager): RecipeApiClient {
        return RecipeApiClient(tokenStorageManager)
    }

    @Provides
    @Singleton
    @Named("local")
    fun provideLocalRecipeDataSource(dataSource: RoomRecipeDataSource): RecipeDataSource {
        return dataSource
    }

    @Provides
    @Singleton
    @Named("remote")
    fun provideRemoteRecipeDataSource(dataSource: RemoteRecipeDataSource): RecipeDataSource {
        return dataSource
    }
}
