package com.labs.systemdesignandroid.domain.usecase

import com.labs.systemdesignandroid.domain.model.MovieModel
import com.labs.systemdesignandroid.domain.repository.MovieRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

class MovieUseCasesTest {

    @Mock
    private lateinit var repository: MovieRepository

    private val testMovie = MovieModel(
        id = 1,
        name = "Inception",
        genres = listOf("Sci-Fi"),
        durationMinutes = 148,
        rating = 8.8,
        year = 2010,
        imageUrl = "",
        description = "Dream thief",
        isFavorite = false,
        isInWatchlist = false
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `AddMovieUseCase calls repository`() = runTest {
        val useCase = AddMovieUseCase(repository)
        useCase(testMovie)
        verify(repository).addMovie(testMovie)
    }

    @Test
    fun `DeleteMovieUseCase calls repository`() = runTest {
        val useCase = DeleteMovieUseCase(repository)
        useCase(testMovie)
        verify(repository).deleteMovie(testMovie)
    }

    @Test
    fun `ToggleFavoriteUseCase updates repository with toggled value`() = runTest {
        val useCase = ToggleFavoriteUseCase(repository)
        useCase(testMovie) // current isFavorite = false
        verify(repository).updateMovie(testMovie.copy(isFavorite = true))
    }

    @Test
    fun `ToggleWatchlistUseCase updates repository with toggled value`() = runTest {
        val useCase = ToggleWatchlistUseCase(repository)
        useCase(testMovie) // current isInWatchlist = false
        verify(repository).updateMovie(testMovie.copy(isInWatchlist = true))
    }
}
