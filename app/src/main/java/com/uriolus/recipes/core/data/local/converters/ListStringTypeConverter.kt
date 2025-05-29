package com.uriolus.recipes.core.data.local.converters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ListStringTypeConverter {
    @TypeConverter
    fun fromListString(list: List<String>?): String? {
        return list?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toListString(jsonString: String?): List<String>? {
        // Ensure empty list if jsonString is null or empty, or decoding fails
        return if (jsonString.isNullOrEmpty()) {
            emptyList()
        } else {
            try {
                Json.decodeFromString<List<String>>(jsonString)
            } catch (e: Exception) {
                // Log error or handle as appropriate
                emptyList() // Fallback to empty list on error
            }
        }
    }
}
