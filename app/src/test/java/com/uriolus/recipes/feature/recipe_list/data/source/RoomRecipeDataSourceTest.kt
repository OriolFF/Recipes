package com.uriolus.recipes.feature.recipe_list.data.source

import app.cash.turbine.test
import com.uriolus.recipes.feature.links_list.data.source.local.RecipeLinkDao
import com.uriolus.recipes.feature.links_list.data.source.local.RecipeLinkEntity
import com.uriolus.recipes.core.data.local.RecipesDatabase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.seconds

class RoomRecipeDataSourceTest {
    
    private lateinit var database: RecipesDatabase
    private lateinit var recipeLinkDao: RecipeLinkDao
    private lateinit var dataSource: RoomRecipeDataSource
    
    @BeforeEach
    fun setup() {
        recipeLinkDao = mockk()
        database = mockk {
            every { recipeLinkDao } returns this@RoomRecipeDataSourceTest.recipeLinkDao
        }
        dataSource = RoomRecipeDataSource(database)
    }
    
    @Test
    fun testRecipeEntityMapping() = runTest {
        // Given
        val testEntity = RecipeLinkEntity(
            id = 1,
            url = "https://example.com/recipe",
            title = "Test Recipe",
            description = "A delicious test recipe\n\nIngredients:\n- 1 cup flour\n- 2 eggs\n\nInstructions:\n1. Mix ingredients\n2. Bake for 30 minutes",
            thumbnailUrl = "https://example.com/image.jpg",
            createdAt = 1620000000000
        )
        
        every { recipeLinkDao.getAllLinks() } returns flowOf(listOf(testEntity))
        
        // When & Then
        dataSource.getRecipes().test(timeout = 3.seconds) {
            val recipes = awaitItem()
            assertEquals(1, recipes.size)
            
            val recipe = recipes[0]
            assertEquals("1", recipe.id)
            assertEquals("Test Recipe", recipe.name)
            assertEquals("A delicious test recipe\n\nIngredients:\n- 1 cup flour\n- 2 eggs\n\nInstructions:\n1. Mix ingredients\n2. Bake for 30 minutes", recipe.description)
            assertEquals("https://example.com/image.jpg", recipe.imageUrl)
            assertEquals(2, recipe.ingredients.size)
            assertEquals("1 cup flour", recipe.ingredients[0])
            assertEquals("2 eggs", recipe.ingredients[1])
            assertEquals(2, recipe.instructions.size)
            assertEquals("Mix ingredients", recipe.instructions[0])
            assertEquals("Bake for 30 minutes", recipe.instructions[1])
            
            awaitComplete()
        }
    }
    
    @Test
    fun testDefaultImageUrl() = runTest {
        // Given
        val testEntity = RecipeLinkEntity(
            id = 1,
            url = "https://example.com/recipe",
            title = "Test Recipe",
            description = "A delicious test recipe",
            thumbnailUrl = "",
            createdAt = 1620000000000
        )
        
        every { recipeLinkDao.getAllLinks() } returns flowOf(listOf(testEntity))
        
        // When & Then
        dataSource.getRecipes().test {
            val recipes = awaitItem()
            assertEquals(1, recipes.size)
            
            val recipe = recipes[0]
            assertEquals("1", recipe.id)
            assertEquals("Test Recipe", recipe.name)
            // Verify that a default image URL is used
            assertTrue(recipe.imageUrl.isNotEmpty())
            
            awaitComplete()
        }
    }
    
    @Test
    fun testDefaultIngredientsAndInstructions() = runTest {
        // Given
        val testEntity = RecipeLinkEntity(
            id = 1,
            url = "https://example.com/recipe",
            title = "Test Recipe",
            description = "Just a simple description with no structured data",
            thumbnailUrl = "https://example.com/image.jpg",
            createdAt = 1620000000000
        )
        
        every { recipeLinkDao.getAllLinks() } returns flowOf(listOf(testEntity))
        
        // When & Then
        dataSource.getRecipes().test {
            val recipes = awaitItem()
            assertEquals(1, recipes.size)
            
            val recipe = recipes[0]
            assertEquals(1, recipe.ingredients.size)
            assertEquals(1, recipe.instructions.size)
            assertTrue(recipe.ingredients[0].contains("No ingredients"))
            assertTrue(recipe.instructions[0].contains("No instructions"))
            
            awaitComplete()
        }
    }
}
