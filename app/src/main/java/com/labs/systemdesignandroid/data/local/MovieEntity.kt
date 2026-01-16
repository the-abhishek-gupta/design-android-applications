package com.labs.systemdesignandroid.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.labs.systemdesignandroid.domain.model.MovieModel

@Entity(tableName = "movies")
data class MovieCatalogEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val genres: String,
    val durationMinutes: Int,
    val rating: Double,
    val year: Int,
    val imageUrl: String,
    val description: String
)