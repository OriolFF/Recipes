package com.uriolus.recipes.feature.links_list.di

import android.app.Application
import androidx.room.Room
import com.uriolus.recipes.feature.links_list.data.repository.RecipeLinkRepositoryImpl
import com.uriolus.recipes.feature.links_list.data.source.local.RecipeLinkDao
import com.uriolus.recipes.core.data.local.RecipesDatabase
import com.uriolus.recipes.feature.links_list.domain.repository.RecipeLinkRepository
import com.uriolus.recipes.feature.links_list.domain.use_case.AddLinkUseCase
import com.uriolus.recipes.feature.links_list.domain.use_case.DeleteLinkUseCase
import com.uriolus.recipes.feature.links_list.domain.use_case.GetAllLinksUseCase
import com.uriolus.recipes.feature.links_list.domain.use_case.LinkUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LinksListModule {

    @Provides
    @Singleton
    fun provideRecipesDatabase(app: Application): RecipesDatabase {
        return Room.databaseBuilder(
            app,
            RecipesDatabase::class.java,
            RecipesDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideRecipeLinkDao(database: RecipesDatabase): RecipeLinkDao {
        return database.recipeLinkDao
    }

    @Provides
    @Singleton
    fun provideRecipeLinkRepository(dao: RecipeLinkDao): RecipeLinkRepository {
        return RecipeLinkRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideLinkUseCases(repository: RecipeLinkRepository): LinkUseCases {
        return LinkUseCases(
            getAllLinks = GetAllLinksUseCase(repository),
            addLink = AddLinkUseCase(repository),
            deleteLink = DeleteLinkUseCase(repository)
        )
    }
}
