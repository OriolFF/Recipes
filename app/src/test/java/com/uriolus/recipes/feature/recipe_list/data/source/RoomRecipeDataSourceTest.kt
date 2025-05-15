package com.uriolus.recipes.feature.recipe_list.data.source

import com.uriolus.recipes.feature.links_list.data.source.local.RecipeLinkDao
import com.uriolus.recipes.feature.links_list.data.source.local.RecipeLinkEntity
import com.uriolus.recipes.feature.links_list.data.source.local.RecipesDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class RoomRecipeDataSourceTest {
    
    private lateinit var database: RecipesDatabase
    private lateinit var recipeLinkDao: RecipeLinkDao
    private lateinit var dataSource: RoomRecipeDataSource
    
    @Before
    fun setup() {
        recipeLinkDao = mock(RecipeLinkDao::class.java)
        database = mock(RecipesDatabase::class.java)
        `when`(database.recipeLinkDao).thenReturn(recipeLinkDao)
        dataSource = RoomRecipeDataSource(database)
    }
    
    @Test
    fun `getRecipes maps RecipeLinkEntity to Recipe correctly`() = runBlocking {
        // Given
        val testEntity = RecipeLinkEntity(
            id = 1,
            url = "https://example.com/recipe",
            title = "Test Recipe",
            description = "A delicious test recipe\n\nIngredients:\n- 1 cup flour\n- 2 eggs\n\nInstructions:\n1. Mix ingredients\n2. Bake for 30 minutes",
            thumbnailUrl = "https://example.com/image.jpg",
            createdAt = 1620000000000
        )
        
        `when`(recipeLinkDao.getAllLinks()).thenReturn(flowOf(listOf(testEntity)))
        
        // When
        val recipes = dataSource.getRecipes().first()
        
        // Then
        assertEquals(1, recipes.size)
        val recipe = recipes[0]
        assertEquals("1", recipe.id)
        assertEquals("Test Recipe", recipe.title)
        assertEquals("A delicious test recipe\n\nIngredients:\n- 1 cup flour\n- 2 eggs\n\nInstructions:\n1. Mix ingredients\n2. Bake for 30 minutes", recipe.description)
        assertEquals("https://example.com/image.jpg", recipe.imageUrl)
        assertEquals(2, recipe.ingredients.size)
        assertEquals("1 cup flour", recipe.ingredients[0])
        assertEquals("2 eggs", recipe.ingredients[1])
        assertEquals(2, recipe.instructions.size)
        assertEquals("Mix ingredients", recipe.instructions[0])
        assertEquals("Bake for 30 minutes", recipe.instructions[1])
    }
    
    @Test
    fun `getRecipes uses default image URL when thumbnailUrl is empty`() = runBlocking {
        // Given
        val testEntity = RecipeLinkEntity(
            id = 1,
            url = "https://example.com/recipe",
            title = "Test Recipe",
            description = "A delicious test recipe",
            thumbnailUrl = "",
            createdAt = 1620000000000
        )
        
        `when`(recipeLinkDao.getAllLinks()).thenReturn(flowOf(listOf(testEntity)))
        
        // When
        val recipes = dataSource.getRecipes().first()
        
        // Then
        assertEquals(1, recipes.size)
        val recipe = recipes[0]
        assertEquals("1", recipe.id)
        assertEquals("Test Recipe", recipe.title)
        // Verify that a default image URL is used
        assert(recipe.imageUrl.isNotEmpty())
    }
    
    @Test
    fun `getRecipes provides default values when no ingredients or instructions are found`() = runBlocking {
        // Given
        val testEntity = RecipeLinkEntity(
            id = 1,
            url = "https://example.com/recipe",
            title = "Test Recipe",
            description = "Just a simple description with no structured data",
            thumbnailUrl = "https://example.com/image.jpg",
            createdAt = 1620000000000
        )
        
        `when`(recipeLinkDao.getAllLinks()).thenReturn(flowOf(listOf(testEntity)))
        
        // When
        val recipes = dataSource.getRecipes().first()
        
        // Then
        assertEquals(1, recipes.size)
        val recipe = recipes[0]
        assertEquals(1, recipe.ingredients.size)
        assertEquals(1, recipe.instructions.size)
        assert(recipe.ingredients[0].contains("No ingredients"))
        assert(recipe.instructions[0].contains("No instructions"))
    }
}
