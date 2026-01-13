package com.labs.systemdesignandroid

import app.cash.turbine.test
import com.labs.systemdesignandroid.domain.model.Movie
import com.labs.systemdesignandroid.domain.SortOrder
import com.labs.systemdesignandroid.domain.usecase.AddMovieUseCase
import com.labs.systemdesignandroid.domain.usecase.DeleteMovieUseCase
import com.labs.systemdesignandroid.domain.usecase.GetMoviesUseCase
import com.labs.systemdesignandroid.domain.usecase.ToggleFavoriteUseCase
import com.labs.systemdesignandroid.domain.usecase.ToggleWatchlistUseCase
import com.labs.systemdesignandroid.domain.usecase.UpdateMovieUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class MovieViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var getMoviesUseCase: GetMoviesUseCase
    @Mock
    private lateinit var addMovieUseCase: AddMovieUseCase
    @Mock
    private lateinit var deleteMovieUseCase: DeleteMovieUseCase
    @Mock
    private lateinit var updateMovieUseCase: UpdateMovieUseCase
    @Mock
    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase
    @Mock
    private lateinit var toggleWatchlistUseCase: ToggleWatchlistUseCase

    private lateinit var viewModel: MovieViewModel

    private val testMovies = listOf(
        Movie(1, "Inception", listOf("Sci-Fi"), 148, 8.8, 2010, "", "Dream thief"),
        Movie(2, "The Godfather", listOf("Crime"), 175, 9.2, 1972, "", "Mafia")
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        
        whenever(getMoviesUseCase()).thenReturn(flowOf(testMovies))
        
        viewModel = MovieViewModel(
            getMoviesUseCase,
            addMovieUseCase,
            deleteMovieUseCase,
            updateMovieUseCase,
            toggleFavoriteUseCase,
            toggleWatchlistUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial movies are loaded correctly`() = runTest {
        viewModel.movies.test {
            assertEquals(emptyList<Movie>(), awaitItem()) // Initial value
            assertEquals(testMovies, awaitItem())
        }
    }

    @Test
    fun `search filters movies correctly`() = runTest {
        viewModel.movies.test {
            awaitItem() // initial
            awaitItem() // loaded
            
            viewModel.onSearchQueryChanged("Inception")
            val filtered = awaitItem()
            assertEquals(1, filtered.size)
            assertEquals("Inception", filtered[0].name)
        }
    }

    @Test
    fun `sorting works correctly`() = runTest {
        viewModel.movies.test {
            awaitItem()
            awaitItem()
            
            viewModel.onSortOrderSelected(SortOrder.NAME_DESC)
            val sorted = awaitItem()
            assertEquals("The Godfather", sorted[0].name)
            assertEquals("Inception", sorted[1].name)
        }
    }
}
