package com.labs.systemdesignandroid.data.repository

import com.labs.systemdesignandroid.data.local.MovieDao
import com.labs.systemdesignandroid.data.local.toDomain
import com.labs.systemdesignandroid.data.local.toEntity
import com.labs.systemdesignandroid.domain.Movie
import com.labs.systemdesignandroid.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val movieDao: MovieDao
) : MovieRepository {

    override fun getMovies(): Flow<List<Movie>> {
        return movieDao.getAllMovies()
            .map { entities -> entities.map { it.toDomain() } }
            .onStart {
                initializeDatabaseIfNeeded()
            }
    }

    override suspend fun addMovie(movie: Movie) {
        movieDao.insertMovies(listOf(movie.toEntity()))
    }

    override suspend fun deleteMovie(movie: Movie) {
        movieDao.deleteMovie(movie.toEntity())
    }

    override suspend fun updateMovie(movie: Movie) {
        movieDao.updateMovie(movie.toEntity())
    }

    private suspend fun initializeDatabaseIfNeeded() {
        if (movieDao.getMovieCount() == 0) {
            val jsonString = """
            {
              "movies": [
                {
                  "id": 1,
                  "name": "The Shawshank Redemption (1)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 142,
                  "rating": 9.2,
                  "year": 1995,
                  "imageUrl": "https://picsum.photos/300/450?random=1",
                  "description": "Two imprisoned men bond over years, finding solace and redemption through acts of common decency."
                },
                {
                  "id": 2,
                  "name": "The Godfather (2)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 175,
                  "rating": 9.0,
                  "year": 1974,
                  "imageUrl": "https://picsum.photos/300/450?random=2",
                  "description": "The aging patriarch of an organized crime dynasty transfers control to his reluctant son."
                },
                {
                  "id": 3,
                  "name": "The Dark Knight (3)",
                  "genres": [
                    "Action",
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 152,
                  "rating": 9.0,
                  "year": 2011,
                  "imageUrl": "https://picsum.photos/300/450?random=3",
                  "description": "Batman faces the Joker, a criminal mastermind who plunges Gotham into chaos."
                },
                {
                  "id": 4,
                  "name": "Pulp Fiction (4)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 154,
                  "rating": 8.8,
                  "year": 1998,
                  "imageUrl": "https://picsum.photos/300/450?random=4",
                  "description": "Interconnected stories of crime, redemption, and violence in Los Angeles."
                },
                {
                  "id": 5,
                  "name": "Forrest Gump (5)",
                  "genres": [
                    "Drama",
                    "Romance"
                  ],
                  "durationMinutes": 142,
                  "rating": 8.6,
                  "year": 1999,
                  "imageUrl": "https://picsum.photos/300/450?random=5",
                  "description": "The life journey of a simple man with a big heart through historic events."
                },
                {
                  "id": 6,
                  "name": "Inception (6)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 148,
                  "rating": 8.8,
                  "year": 2016,
                  "imageUrl": "https://picsum.photos/300/450?random=6",
                  "description": "A skilled thief steals information by infiltrating dreams within dreams."
                },
                {
                  "id": 7,
                  "name": "Fight Club (7)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 139,
                  "rating": 8.7,
                  "year": 2006,
                  "imageUrl": "https://picsum.photos/300/450?random=7",
                  "description": "An insomniac office worker forms an underground fight club that spirals out of control."
                },
                {
                  "id": 8,
                  "name": "Interstellar (8)",
                  "genres": [
                    "Sci-Fi",
                    "Drama"
                  ],
                  "durationMinutes": 169,
                  "rating": 8.5,
                  "year": 2022,
                  "imageUrl": "https://picsum.photos/300/450?random=8",
                  "description": "Explorers travel through a wormhole in space to save humanity."
                },
                {
                  "id": 9,
                  "name": "The Matrix (9)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 136,
                  "rating": 8.7,
                  "year": 2008,
                  "imageUrl": "https://picsum.photos/300/450?random=9",
                  "description": "A hacker learns the truth about reality and his role in the war against its controllers."
                },
                {
                  "id": 10,
                  "name": "Gladiator (10)",
                  "genres": [
                    "Action",
                    "Drama"
                  ],
                  "durationMinutes": 155,
                  "rating": 8.4,
                  "year": 2000,
                  "imageUrl": "https://picsum.photos/300/450?random=10",
                  "description": "A betrayed Roman general seeks revenge against a corrupt emperor."
                },
                {
                  "id": 11,
                  "name": "The Shawshank Redemption (11)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 142,
                  "rating": 9.1,
                  "year": 1995,
                  "imageUrl": "https://picsum.photos/300/450?random=11",
                  "description": "Two imprisoned men bond over years, finding solace and redemption through acts of common decency."
                },
                {
                  "id": 12,
                  "name": "The Godfather (12)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 175,
                  "rating": 9.2,
                  "year": 1974,
                  "imageUrl": "https://picsum.photos/300/450?random=12",
                  "description": "The aging patriarch of an organized crime dynasty transfers control to his reluctant son."
                },
                {
                  "id": 13,
                  "name": "The Dark Knight (13)",
                  "genres": [
                    "Action",
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 152,
                  "rating": 8.9,
                  "year": 2011,
                  "imageUrl": "https://picsum.photos/300/450?random=13",
                  "description": "Batman faces the Joker, a criminal mastermind who plunges Gotham into chaos."
                },
                {
                  "id": 14,
                  "name": "Pulp Fiction (14)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 154,
                  "rating": 8.7,
                  "year": 1998,
                  "imageUrl": "https://picsum.photos/300/450?random=14",
                  "description": "Interconnected stories of crime, redemption, and violence in Los Angeles."
                },
                {
                  "id": 15,
                  "name": "Forrest Gump (15)",
                  "genres": [
                    "Drama",
                    "Romance"
                  ],
                  "durationMinutes": 142,
                  "rating": 8.8,
                  "year": 1999,
                  "imageUrl": "https://picsum.photos/300/450?random=15",
                  "description": "The life journey of a simple man with a big heart through historic events."
                },
                {
                  "id": 16,
                  "name": "Inception (16)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 148,
                  "rating": 8.7,
                  "year": 2016,
                  "imageUrl": "https://picsum.photos/300/450?random=16",
                  "description": "A skilled thief steals information by infiltrating dreams within dreams."
                },
                {
                  "id": 17,
                  "name": "Fight Club (17)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 139,
                  "rating": 8.6,
                  "year": 2006,
                  "imageUrl": "https://picsum.photos/300/450?random=17",
                  "description": "An insomniac office worker forms an underground fight club that spirals out of control."
                },
                {
                  "id": 18,
                  "name": "Interstellar (18)",
                  "genres": [
                    "Sci-Fi",
                    "Drama"
                  ],
                  "durationMinutes": 169,
                  "rating": 8.7,
                  "year": 2022,
                  "imageUrl": "https://picsum.photos/300/450?random=18",
                  "description": "Explorers travel through a wormhole in space to save humanity."
                },
                {
                  "id": 19,
                  "name": "The Matrix (19)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 136,
                  "rating": 8.6,
                  "year": 2008,
                  "imageUrl": "https://picsum.photos/300/450?random=19",
                  "description": "A hacker learns the truth about reality and his role in the war against its controllers."
                },
                {
                  "id": 20,
                  "name": "Gladiator (20)",
                  "genres": [
                    "Action",
                    "Drama"
                  ],
                  "durationMinutes": 155,
                  "rating": 8.3,
                  "year": 2000,
                  "imageUrl": "https://picsum.photos/300/450?random=20",
                  "description": "A betrayed Roman general seeks revenge against a corrupt emperor."
                },
                {
                  "id": 21,
                  "name": "The Shawshank Redemption (21)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 142,
                  "rating": 9.3,
                  "year": 1995,
                  "imageUrl": "https://picsum.photos/300/450?random=21",
                  "description": "Two imprisoned men bond over years, finding solace and redemption through acts of common decency."
                },
                {
                  "id": 22,
                  "name": "The Godfather (22)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 175,
                  "rating": 9.1,
                  "year": 1974,
                  "imageUrl": "https://picsum.photos/300/450?random=22",
                  "description": "The aging patriarch of an organized crime dynasty transfers control to his reluctant son."
                },
                {
                  "id": 23,
                  "name": "The Dark Knight (23)",
                  "genres": [
                    "Action",
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 152,
                  "rating": 8.8,
                  "year": 2011,
                  "imageUrl": "https://picsum.photos/300/450?random=23",
                  "description": "Batman faces the Joker, a criminal mastermind who plunges Gotham into chaos."
                },
                {
                  "id": 24,
                  "name": "Pulp Fiction (24)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 154,
                  "rating": 8.9,
                  "year": 1998,
                  "imageUrl": "https://picsum.photos/300/450?random=24",
                  "description": "Interconnected stories of crime, redemption, and violence in Los Angeles."
                },
                {
                  "id": 25,
                  "name": "Forrest Gump (25)",
                  "genres": [
                    "Drama",
                    "Romance"
                  ],
                  "durationMinutes": 142,
                  "rating": 8.7,
                  "year": 1999,
                  "imageUrl": "https://picsum.photos/300/450?random=25",
                  "description": "The life journey of a simple man with a big heart through historic events."
                },
                {
                  "id": 26,
                  "name": "Inception (26)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 148,
                  "rating": 8.6,
                  "year": 2016,
                  "imageUrl": "https://picsum.photos/300/450?random=26",
                  "description": "A skilled thief steals information by infiltrating dreams within dreams."
                },
                {
                  "id": 27,
                  "name": "Fight Club (27)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 139,
                  "rating": 8.8,
                  "year": 2006,
                  "imageUrl": "https://picsum.photos/300/450?random=27",
                  "description": "An insomniac office worker forms an underground fight club that spirals out of control."
                },
                {
                  "id": 28,
                  "name": "Interstellar (28)",
                  "genres": [
                    "Sci-Fi",
                    "Drama"
                  ],
                  "durationMinutes": 169,
                  "rating": 8.6,
                  "year": 2022,
                  "imageUrl": "https://picsum.photos/300/450?random=28",
                  "description": "Explorers travel through a wormhole in space to save humanity."
                },
                {
                  "id": 29,
                  "name": "The Matrix (29)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 136,
                  "rating": 8.5,
                  "year": 2008,
                  "imageUrl": "https://picsum.photos/300/450?random=29",
                  "description": "A hacker learns the truth about reality and his role in the war against its controllers."
                },
                {
                  "id": 30,
                  "name": "Gladiator (30)",
                  "genres": [
                    "Action",
                    "Drama"
                  ],
                  "durationMinutes": 155,
                  "rating": 8.5,
                  "year": 2000,
                  "imageUrl": "https://picsum.photos/300/450?random=30",
                  "description": "A betrayed Roman general seeks revenge against a corrupt emperor."
                }
              ]
            }
            """.trimIndent()

            val json = Json { ignoreUnknownKeys = true }
            val root = json.parseToJsonElement(jsonString).jsonObject
            val movieArray = root["movies"] ?: return
            val movies: List<Movie> = json.decodeFromJsonElement(movieArray)
            
            movieDao.insertMovies(movies.map { it.toEntity() })
        }
    }
}
