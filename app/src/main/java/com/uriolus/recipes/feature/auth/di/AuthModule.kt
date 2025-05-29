package com.uriolus.recipes.feature.auth.di

import com.uriolus.recipes.feature.auth.data.repository.AuthRepositoryImpl
import com.uriolus.recipes.feature.auth.domain.repository.AuthRepository
import com.uriolus.recipes.feature.recipe_list.data.source.remote.RecipeApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(recipeApiClient: RecipeApiClient): AuthRepository {
        return AuthRepositoryImpl(recipeApiClient)
    }

    // LoginUseCase can be constructor-injected directly by Hilt
    // as AuthRepository is now provided by this module.
}
