package com.labs.systemdesignandroid.data.local

import androidx.room.Embedded
import androidx.room.Relation

data class MovieWithState(
    @Embedded val movie: MovieCatalogEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "movieId"
    )
    val state: UserMovieStateEntity?
)
