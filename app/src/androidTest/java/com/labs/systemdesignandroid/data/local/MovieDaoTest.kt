package com.labs.systemdesignandroid.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: MovieDao

    @Before
    fun setup() {
        // Use an in-memory database for testing
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.movieDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndReadMovie() = runTest {
        val movie = createTestMovie(id = 1, name = "Test Movie")
        dao.insertMovies(listOf(movie))

        val allMovies = dao.getAllMovies().first()
        assertEquals(1, allMovies.size)
        assertEquals("Test Movie", allMovies[0].name)
    }

    @Test
    fun updateMovie_updatesStateCorrectly() = runTest {
        val movie = createTestMovie(id = 1, isFavorite = false)
        dao.insertMovies(listOf(movie))

        val updatedMovie = movie.copy(isFavorite = true)
        dao.updateMovie(updatedMovie)

        val allMovies = dao.getAllMovies().first()
        assertTrue(allMovies[0].isFavorite)
    }

    @Test
    fun deleteMovie_removesFromDatabase() = runTest {
        val movie = createTestMovie(id = 1)
        dao.insertMovies(listOf(movie))
        
        dao.deleteMovie(movie)

        val allMovies = dao.getAllMovies().first()
        assertTrue(allMovies.isEmpty())
    }

    @Test
    fun getMovieCount_returnsCorrectCount() = runTest {
        val movies = listOf(
            createTestMovie(id = 1),
            createTestMovie(id = 2),
            createTestMovie(id = 3)
        )
        dao.insertMovies(movies)

        val count = dao.getMovieCount()
        assertEquals(3, count)
    }

    private fun createTestMovie(
        id: Int,
        name: String = "Movie",
        isFavorite: Boolean = false,
        isInWatchlist: Boolean = false
    ) = MovieEntity(
        id = id,
        name = name,
        genres = "Drama",
        durationMinutes = 120,
        rating = 8.0,
        year = 2024,
        imageUrl = "",
        description = "Description",
        isFavorite = isFavorite,
        isInWatchlist = isInWatchlist
    )
}
