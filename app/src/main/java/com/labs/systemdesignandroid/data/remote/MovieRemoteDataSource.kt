package com.labs.systemdesignandroid.data.remote

import com.labs.systemdesignandroid.data.local.MovieCatalogEntity

interface MovieRemoteDataSource {
    suspend fun fetchCatalog(): List<MovieCatalogEntity>
}
