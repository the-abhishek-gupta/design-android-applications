package com.labs.systemdesignandroid.data.repository

import app.cash.turbine.test
import com.labs.systemdesignandroid.data.local.MovieDao
import com.labs.systemdesignandroid.data.local.MovieEntity
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class MovieRepositoryImplTest {

    @Mock
    private lateinit var movieDao: MovieDao

    private lateinit var repository: MovieRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = MovieRepositoryImpl(movieDao)
    }

    @Test
    fun `getMovies emits mapped domain movies from dao`() = runTest {
        // Given
        val movieEntities = listOf(
            MovieEntity(
                id = 1,
                name = "Inception",
                genres = "Sci-Fi,Action",
                durationMinutes = 148,
                rating = 8.8,
                year = 2010,
                imageUrl = "",
                description = "Dream thief",
                isFavorite = false,
                isInWatchlist = false
            )
        )
        // Mock getMovieCount to return a non-zero value so seeding logic doesn't run during basic data retrieval tests
        whenever(movieDao.getMovieCount()).thenReturn(1)
        whenever(movieDao.getAllMovies()).thenReturn(flowOf(movieEntities))

        // When/Then
        repository.getMovies().test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("Inception", result[0].name)
            assertEquals(listOf("Sci-Fi", "Action"), result[0].genres)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
