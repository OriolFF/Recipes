package com.uriolus.recipes.feature.links_list.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeLinkDao {
    @Query("SELECT * FROM recipe_links ORDER BY createdAt DESC")
    fun getAllLinks(): Flow<List<RecipeLinkEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLink(link: RecipeLinkEntity): Long
    
    @Delete
    suspend fun deleteLink(link: RecipeLinkEntity)
    
    @Query("DELETE FROM recipe_links WHERE id = :linkId")
    suspend fun deleteLinkById(linkId: Long)

    @Query("DELETE FROM recipe_links")
    suspend fun deleteAll()
}
