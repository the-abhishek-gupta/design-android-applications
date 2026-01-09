package com.labs.systemdesignandroid.data

import com.labs.systemdesignandroid.data.local.MovieDao
import com.labs.systemdesignandroid.data.local.toDomain
import com.labs.systemdesignandroid.data.local.toEntity
import com.labs.systemdesignandroid.domain.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import javax.inject.Inject
import javax.inject.Singleton

interface MovieRepository {
    fun getMovies(): Flow<List<Movie>>
    suspend fun addMovie(movie: Movie)
    suspend fun deleteMovie(movie: Movie)
}

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
                },
                {
                  "id": 31,
                  "name": "The Shawshank Redemption (31)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 142,
                  "rating": 9.2,
                  "year": 1995,
                  "imageUrl": "https://picsum.photos/300/450?random=31",
                  "description": "Two imprisoned men bond over years, finding solace and redemption through acts of common decency."
                },
                {
                  "id": 32,
                  "name": "The Godfather (32)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 175,
                  "rating": 9.0,
                  "year": 1974,
                  "imageUrl": "https://picsum.photos/300/450?random=32",
                  "description": "The aging patriarch of an organized crime dynasty transfers control to his reluctant son."
                },
                {
                  "id": 33,
                  "name": "The Dark Knight (33)",
                  "genres": [
                    "Action",
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 152,
                  "rating": 9.0,
                  "year": 2011,
                  "imageUrl": "https://picsum.photos/300/450?random=33",
                  "description": "Batman faces the Joker, a criminal mastermind who plunges Gotham into chaos."
                },
                {
                  "id": 34,
                  "name": "Pulp Fiction (34)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 154,
                  "rating": 8.8,
                  "year": 1998,
                  "imageUrl": "https://picsum.photos/300/450?random=34",
                  "description": "Interconnected stories of crime, redemption, and violence in Los Angeles."
                },
                {
                  "id": 35,
                  "name": "Forrest Gump (35)",
                  "genres": [
                    "Drama",
                    "Romance"
                  ],
                  "durationMinutes": 142,
                  "rating": 8.6,
                  "year": 1999,
                  "imageUrl": "https://picsum.photos/300/450?random=35",
                  "description": "The life journey of a simple man with a big heart through historic events."
                },
                {
                  "id": 36,
                  "name": "Inception (36)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 148,
                  "rating": 8.8,
                  "year": 2016,
                  "imageUrl": "https://picsum.photos/300/450?random=36",
                  "description": "A skilled thief steals information by infiltrating dreams within dreams."
                },
                {
                  "id": 37,
                  "name": "Fight Club (37)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 139,
                  "rating": 8.7,
                  "year": 2006,
                  "imageUrl": "https://picsum.photos/300/450?random=37",
                  "description": "An insomniac office worker forms an underground fight club that spirals out of control."
                },
                {
                  "id": 38,
                  "name": "Interstellar (38)",
                  "genres": [
                    "Sci-Fi",
                    "Drama"
                  ],
                  "durationMinutes": 169,
                  "rating": 8.5,
                  "year": 2022,
                  "imageUrl": "https://picsum.photos/300/450?random=38",
                  "description": "Explorers travel through a wormhole in space to save humanity."
                },
                {
                  "id": 39,
                  "name": "The Matrix (39)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 136,
                  "rating": 8.7,
                  "year": 2008,
                  "imageUrl": "https://picsum.photos/300/450?random=39",
                  "description": "A hacker learns the truth about reality and his role in the war against its controllers."
                },
                {
                  "id": 40,
                  "name": "Gladiator (40)",
                  "genres": [
                    "Action",
                    "Drama"
                  ],
                  "durationMinutes": 155,
                  "rating": 8.4,
                  "year": 2000,
                  "imageUrl": "https://picsum.photos/300/450?random=40",
                  "description": "A betrayed Roman general seeks revenge against a corrupt emperor."
                },
                {
                  "id": 41,
                  "name": "The Shawshank Redemption (41)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 142,
                  "rating": 9.1,
                  "year": 1995,
                  "imageUrl": "https://picsum.photos/300/450?random=41",
                  "description": "Two imprisoned men bond over years, finding solace and redemption through acts of common decency."
                },
                {
                  "id": 42,
                  "name": "The Godfather (42)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 175,
                  "rating": 9.2,
                  "year": 1974,
                  "imageUrl": "https://picsum.photos/300/450?random=42",
                  "description": "The aging patriarch of an organized crime dynasty transfers control to his reluctant son."
                },
                {
                  "id": 43,
                  "name": "The Dark Knight (43)",
                  "genres": [
                    "Action",
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 152,
                  "rating": 8.9,
                  "year": 2011,
                  "imageUrl": "https://picsum.photos/300/450?random=43",
                  "description": "Batman faces the Joker, a criminal mastermind who plunges Gotham into chaos."
                },
                {
                  "id": 44,
                  "name": "Pulp Fiction (44)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 154,
                  "rating": 8.7,
                  "year": 1998,
                  "imageUrl": "https://picsum.photos/300/450?random=44",
                  "description": "Interconnected stories of crime, redemption, and violence in Los Angeles."
                },
                {
                  "id": 45,
                  "name": "Forrest Gump (45)",
                  "genres": [
                    "Drama",
                    "Romance"
                  ],
                  "durationMinutes": 142,
                  "rating": 8.8,
                  "year": 1999,
                  "imageUrl": "https://picsum.photos/300/450?random=45",
                  "description": "The life journey of a simple man with a big heart through historic events."
                },
                {
                  "id": 46,
                  "name": "Inception (46)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 148,
                  "rating": 8.7,
                  "year": 2016,
                  "imageUrl": "https://picsum.photos/300/450?random=46",
                  "description": "A skilled thief steals information by infiltrating dreams within dreams."
                },
                {
                  "id": 47,
                  "name": "Fight Club (47)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 139,
                  "rating": 8.6,
                  "year": 2006,
                  "imageUrl": "https://picsum.photos/300/450?random=47",
                  "description": "An insomniac office worker forms an underground fight club that spirals out of control."
                },
                {
                  "id": 48,
                  "name": "Interstellar (48)",
                  "genres": [
                    "Sci-Fi",
                    "Drama"
                  ],
                  "durationMinutes": 169,
                  "rating": 8.7,
                  "year": 2022,
                  "imageUrl": "https://picsum.photos/300/450?random=48",
                  "description": "Explorers travel through a wormhole in space to save humanity."
                },
                {
                  "id": 49,
                  "name": "The Matrix (49)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 136,
                  "rating": 8.6,
                  "year": 2008,
                  "imageUrl": "https://picsum.photos/300/450?random=49",
                  "description": "A hacker learns the truth about reality and his role in the war against its controllers."
                },
                {
                  "id": 50,
                  "name": "Gladiator (50)",
                  "genres": [
                    "Action",
                    "Drama"
                  ],
                  "durationMinutes": 155,
                  "rating": 8.3,
                  "year": 2000,
                  "imageUrl": "https://picsum.photos/300/450?random=50",
                  "description": "A betrayed Roman general seeks revenge against a corrupt emperor."
                },
                {
                  "id": 51,
                  "name": "The Shawshank Redemption (51)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 142,
                  "rating": 9.3,
                  "year": 1995,
                  "imageUrl": "https://picsum.photos/300/450?random=51",
                  "description": "Two imprisoned men bond over years, finding solace and redemption through acts of common decency."
                },
                {
                  "id": 52,
                  "name": "The Godfather (52)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 175,
                  "rating": 9.1,
                  "year": 1974,
                  "imageUrl": "https://picsum.photos/300/450?random=52",
                  "description": "The aging patriarch of an organized crime dynasty transfers control to his reluctant son."
                },
                {
                  "id": 53,
                  "name": "The Dark Knight (53)",
                  "genres": [
                    "Action",
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 152,
                  "rating": 8.8,
                  "year": 2011,
                  "imageUrl": "https://picsum.photos/300/450?random=53",
                  "description": "Batman faces the Joker, a criminal mastermind who plunges Gotham into chaos."
                },
                {
                  "id": 54,
                  "name": "Pulp Fiction (54)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 154,
                  "rating": 8.9,
                  "year": 1998,
                  "imageUrl": "https://picsum.photos/300/450?random=54",
                  "description": "Interconnected stories of crime, redemption, and violence in Los Angeles."
                },
                {
                  "id": 55,
                  "name": "Forrest Gump (55)",
                  "genres": [
                    "Drama",
                    "Romance"
                  ],
                  "durationMinutes": 142,
                  "rating": 8.7,
                  "year": 1999,
                  "imageUrl": "https://picsum.photos/300/450?random=55",
                  "description": "The life journey of a simple man with a big heart through historic events."
                },
                {
                  "id": 56,
                  "name": "Inception (56)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 148,
                  "rating": 8.6,
                  "year": 2016,
                  "imageUrl": "https://picsum.photos/300/450?random=56",
                  "description": "A skilled thief steals information by infiltrating dreams within dreams."
                },
                {
                  "id": 57,
                  "name": "Fight Club (57)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 139,
                  "rating": 8.8,
                  "year": 2006,
                  "imageUrl": "https://picsum.photos/300/450?random=57",
                  "description": "An insomniac office worker forms an underground fight club that spirals out of control."
                },
                {
                  "id": 58,
                  "name": "Interstellar (58)",
                  "genres": [
                    "Sci-Fi",
                    "Drama"
                  ],
                  "durationMinutes": 169,
                  "rating": 8.6,
                  "year": 2022,
                  "imageUrl": "https://picsum.photos/300/450?random=58",
                  "description": "Explorers travel through a wormhole in space to save humanity."
                },
                {
                  "id": 59,
                  "name": "The Matrix (59)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 136,
                  "rating": 8.5,
                  "year": 2008,
                  "imageUrl": "https://picsum.photos/300/450?random=59",
                  "description": "A hacker learns the truth about reality and his role in the war against its controllers."
                },
                {
                  "id": 60,
                  "name": "Gladiator (60)",
                  "genres": [
                    "Action",
                    "Drama"
                  ],
                  "durationMinutes": 155,
                  "rating": 8.5,
                  "year": 2000,
                  "imageUrl": "https://picsum.photos/300/450?random=60",
                  "description": "A betrayed Roman general seeks revenge against a corrupt emperor."
                },
                {
                  "id": 61,
                  "name": "The Shawshank Redemption (61)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 142,
                  "rating": 9.2,
                  "year": 1995,
                  "imageUrl": "https://picsum.photos/300/450?random=61",
                  "description": "Two imprisoned men bond over years, finding solace and redemption through acts of common decency."
                },
                {
                  "id": 62,
                  "name": "The Godfather (62)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 175,
                  "rating": 9.0,
                  "year": 1974,
                  "imageUrl": "https://picsum.photos/300/450?random=62",
                  "description": "The aging patriarch of an organized crime dynasty transfers control to his reluctant son."
                },
                {
                  "id": 63,
                  "name": "The Dark Knight (63)",
                  "genres": [
                    "Action",
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 152,
                  "rating": 9.0,
                  "year": 2011,
                  "imageUrl": "https://picsum.photos/300/450?random=63",
                  "description": "Batman faces the Joker, a criminal mastermind who plunges Gotham into chaos."
                },
                {
                  "id": 64,
                  "name": "Pulp Fiction (64)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 154,
                  "rating": 8.8,
                  "year": 1998,
                  "imageUrl": "https://picsum.photos/300/450?random=64",
                  "description": "Interconnected stories of crime, redemption, and violence in Los Angeles."
                },
                {
                  "id": 65,
                  "name": "Forrest Gump (65)",
                  "genres": [
                    "Drama",
                    "Romance"
                  ],
                  "durationMinutes": 142,
                  "rating": 8.6,
                  "year": 1999,
                  "imageUrl": "https://picsum.photos/300/450?random=65",
                  "description": "The life journey of a simple man with a big heart through historic events."
                },
                {
                  "id": 66,
                  "name": "Inception (66)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 148,
                  "rating": 8.8,
                  "year": 2016,
                  "imageUrl": "https://picsum.photos/300/450?random=66",
                  "description": "A skilled thief steals information by infiltrating dreams within dreams."
                },
                {
                  "id": 67,
                  "name": "Fight Club (67)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 139,
                  "rating": 8.7,
                  "year": 2006,
                  "imageUrl": "https://picsum.photos/300/450?random=67",
                  "description": "An insomniac office worker forms an underground fight club that spirals out of control."
                },
                {
                  "id": 68,
                  "name": "Interstellar (68)",
                  "genres": [
                    "Sci-Fi",
                    "Drama"
                  ],
                  "durationMinutes": 169,
                  "rating": 8.5,
                  "year": 2022,
                  "imageUrl": "https://picsum.photos/300/450?random=68",
                  "description": "Explorers travel through a wormhole in space to save humanity."
                },
                {
                  "id": 69,
                  "name": "The Matrix (69)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 136,
                  "rating": 8.7,
                  "year": 2008,
                  "imageUrl": "https://picsum.photos/300/450?random=69",
                  "description": "A hacker learns the truth about reality and his role in the war against its controllers."
                },
                {
                  "id": 70,
                  "name": "Gladiator (70)",
                  "genres": [
                    "Action",
                    "Drama"
                  ],
                  "durationMinutes": 155,
                  "rating": 8.4,
                  "year": 2000,
                  "imageUrl": "https://picsum.photos/300/450?random=70",
                  "description": "A betrayed Roman general seeks revenge against a corrupt emperor."
                },
                {
                  "id": 71,
                  "name": "The Shawshank Redemption (71)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 142,
                  "rating": 9.1,
                  "year": 1995,
                  "imageUrl": "https://picsum.photos/300/450?random=71",
                  "description": "Two imprisoned men bond over years, finding solace and redemption through acts of common decency."
                },
                {
                  "id": 72,
                  "name": "The Godfather (72)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 175,
                  "rating": 9.2,
                  "year": 1974,
                  "imageUrl": "https://picsum.photos/300/450?random=72",
                  "description": "The aging patriarch of an organized crime dynasty transfers control to his reluctant son."
                },
                {
                  "id": 73,
                  "name": "The Dark Knight (73)",
                  "genres": [
                    "Action",
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 152,
                  "rating": 8.9,
                  "year": 2011,
                  "imageUrl": "https://picsum.photos/300/450?random=73",
                  "description": "Batman faces the Joker, a criminal mastermind who plunges Gotham into chaos."
                },
                {
                  "id": 74,
                  "name": "Pulp Fiction (74)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 154,
                  "rating": 8.7,
                  "year": 1998,
                  "imageUrl": "https://picsum.photos/300/450?random=74",
                  "description": "Interconnected stories of crime, redemption, and violence in Los Angeles."
                },
                {
                  "id": 75,
                  "name": "Forrest Gump (75)",
                  "genres": [
                    "Drama",
                    "Romance"
                  ],
                  "durationMinutes": 142,
                  "rating": 8.8,
                  "year": 1999,
                  "imageUrl": "https://picsum.photos/300/450?random=75",
                  "description": "The life journey of a simple man with a big heart through historic events."
                },
                {
                  "id": 76,
                  "name": "Inception (76)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 148,
                  "rating": 8.7,
                  "year": 2016,
                  "imageUrl": "https://picsum.photos/300/450?random=76",
                  "description": "A skilled thief steals information by infiltrating dreams within dreams."
                },
                {
                  "id": 77,
                  "name": "Fight Club (77)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 139,
                  "rating": 8.6,
                  "year": 2006,
                  "imageUrl": "https://picsum.photos/300/450?random=77",
                  "description": "An insomniac office worker forms an underground fight club that spirals out of control."
                },
                {
                  "id": 78,
                  "name": "Interstellar (78)",
                  "genres": [
                    "Sci-Fi",
                    "Drama"
                  ],
                  "durationMinutes": 169,
                  "rating": 8.7,
                  "year": 2022,
                  "imageUrl": "https://picsum.photos/300/450?random=78",
                  "description": "Explorers travel through a wormhole in space to save humanity."
                },
                {
                  "id": 79,
                  "name": "The Matrix (79)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 136,
                  "rating": 8.6,
                  "year": 2008,
                  "imageUrl": "https://picsum.photos/300/450?random=79",
                  "description": "A hacker learns the truth about reality and his role in the war against its controllers."
                },
                {
                  "id": 80,
                  "name": "Gladiator (80)",
                  "genres": [
                    "Action",
                    "Drama"
                  ],
                  "durationMinutes": 155,
                  "rating": 8.3,
                  "year": 2000,
                  "imageUrl": "https://picsum.photos/300/450?random=80",
                  "description": "A betrayed Roman general seeks revenge against a corrupt emperor."
                },
                {
                  "id": 81,
                  "name": "The Shawshank Redemption (81)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 142,
                  "rating": 9.3,
                  "year": 1995,
                  "imageUrl": "https://picsum.photos/300/450?random=81",
                  "description": "Two imprisoned men bond over years, finding solace and redemption through acts of common decency."
                },
                {
                  "id": 82,
                  "name": "The Godfather (82)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 175,
                  "rating": 9.1,
                  "year": 1974,
                  "imageUrl": "https://picsum.photos/300/450?random=82",
                  "description": "The aging patriarch of an organized crime dynasty transfers control to his reluctant son."
                },
                {
                  "id": 83,
                  "name": "The Dark Knight (83)",
                  "genres": [
                    "Action",
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 152,
                  "rating": 8.8,
                  "year": 2011,
                  "imageUrl": "https://picsum.photos/300/450?random=83",
                  "description": "Batman faces the Joker, a criminal mastermind who plunges Gotham into chaos."
                },
                {
                  "id": 84,
                  "name": "Pulp Fiction (84)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 154,
                  "rating": 8.9,
                  "year": 1998,
                  "imageUrl": "https://picsum.photos/300/450?random=84",
                  "description": "Interconnected stories of crime, redemption, and violence in Los Angeles."
                },
                {
                  "id": 85,
                  "name": "Forrest Gump (85)",
                  "genres": [
                    "Drama",
                    "Romance"
                  ],
                  "durationMinutes": 142,
                  "rating": 8.7,
                  "year": 1999,
                  "imageUrl": "https://picsum.photos/300/450?random=85",
                  "description": "The life journey of a simple man with a big heart through historic events."
                },
                {
                  "id": 86,
                  "name": "Inception (86)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 148,
                  "rating": 8.6,
                  "year": 2016,
                  "imageUrl": "https://picsum.photos/300/450?random=86",
                  "description": "A skilled thief steals information by infiltrating dreams within dreams."
                },
                {
                  "id": 87,
                  "name": "Fight Club (87)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 139,
                  "rating": 8.8,
                  "year": 2006,
                  "imageUrl": "https://picsum.photos/300/450?random=87",
                  "description": "An insomniac office worker forms an underground fight club that spirals out of control."
                },
                {
                  "id": 88,
                  "name": "Interstellar (88)",
                  "genres": [
                    "Sci-Fi",
                    "Drama"
                  ],
                  "durationMinutes": 169,
                  "rating": 8.6,
                  "year": 2022,
                  "imageUrl": "https://picsum.photos/300/450?random=88",
                  "description": "Explorers travel through a wormhole in space to save humanity."
                },
                {
                  "id": 89,
                  "name": "The Matrix (89)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 136,
                  "rating": 8.5,
                  "year": 2008,
                  "imageUrl": "https://picsum.photos/300/450?random=89",
                  "description": "A hacker learns the truth about reality and his role in the war against its controllers."
                },
                {
                  "id": 90,
                  "name": "Gladiator (90)",
                  "genres": [
                    "Action",
                    "Drama"
                  ],
                  "durationMinutes": 155,
                  "rating": 8.5,
                  "year": 2000,
                  "imageUrl": "https://picsum.photos/300/450?random=90",
                  "description": "A betrayed Roman general seeks revenge against a corrupt emperor."
                },
                {
                  "id": 91,
                  "name": "The Shawshank Redemption (91)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 142,
                  "rating": 9.2,
                  "year": 1995,
                  "imageUrl": "https://picsum.photos/300/450?random=91",
                  "description": "Two imprisoned men bond over years, finding solace and redemption through acts of common decency."
                },
                {
                  "id": 92,
                  "name": "The Godfather (92)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 175,
                  "rating": 9.0,
                  "year": 1974,
                  "imageUrl": "https://picsum.photos/300/450?random=92",
                  "description": "The aging patriarch of an organized crime dynasty transfers control to his reluctant son."
                },
                {
                  "id": 93,
                  "name": "The Dark Knight (93)",
                  "genres": [
                    "Action",
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 152,
                  "rating": 9.0,
                  "year": 2011,
                  "imageUrl": "https://picsum.photos/300/450?random=93",
                  "description": "Batman faces the Joker, a criminal mastermind who plunges Gotham into chaos."
                },
                {
                  "id": 94,
                  "name": "Pulp Fiction (94)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 154,
                  "rating": 8.8,
                  "year": 1998,
                  "imageUrl": "https://picsum.photos/300/450?random=94",
                  "description": "Interconnected stories of crime, redemption, and violence in Los Angeles."
                },
                {
                  "id": 95,
                  "name": "Forrest Gump (95)",
                  "genres": [
                    "Drama",
                    "Romance"
                  ],
                  "durationMinutes": 142,
                  "rating": 8.6,
                  "year": 1999,
                  "imageUrl": "https://picsum.photos/300/450?random=95",
                  "description": "The life journey of a simple man with a big heart through historic events."
                },
                {
                  "id": 96,
                  "name": "Inception (96)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 148,
                  "rating": 8.8,
                  "year": 2016,
                  "imageUrl": "https://picsum.photos/300/450?random=96",
                  "description": "A skilled thief steals information by infiltrating dreams within dreams."
                },
                {
                  "id": 97,
                  "name": "Fight Club (97)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 139,
                  "rating": 8.7,
                  "year": 2006,
                  "imageUrl": "https://picsum.photos/300/450?random=97",
                  "description": "An insomniac office worker forms an underground fight club that spirals out of control."
                },
                {
                  "id": 98,
                  "name": "Interstellar (98)",
                  "genres": [
                    "Sci-Fi",
                    "Drama"
                  ],
                  "durationMinutes": 169,
                  "rating": 8.5,
                  "year": 2022,
                  "imageUrl": "https://picsum.photos/300/450?random=98",
                  "description": "Explorers travel through a wormhole in space to save humanity."
                },
                {
                  "id": 99,
                  "name": "The Matrix (99)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 136,
                  "rating": 8.7,
                  "year": 2008,
                  "imageUrl": "https://picsum.photos/300/450?random=99",
                  "description": "A hacker learns the truth about reality and his role in the war against its controllers."
                },
                {
                  "id": 100,
                  "name": "Gladiator (100)",
                  "genres": [
                    "Action",
                    "Drama"
                  ],
                  "durationMinutes": 155,
                  "rating": 8.4,
                  "year": 2000,
                  "imageUrl": "https://picsum.photos/300/450?random=100",
                  "description": "A betrayed Roman general seeks revenge against a corrupt emperor."
                },
                {
                  "id": 101,
                  "name": "The Shawshank Redemption (101)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 142,
                  "rating": 9.1,
                  "year": 1995,
                  "imageUrl": "https://picsum.photos/300/450?random=101",
                  "description": "Two imprisoned men bond over years, finding solace and redemption through acts of common decency."
                },
                {
                  "id": 102,
                  "name": "The Godfather (102)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 175,
                  "rating": 9.2,
                  "year": 1974,
                  "imageUrl": "https://picsum.photos/300/450?random=102",
                  "description": "The aging patriarch of an organized crime dynasty transfers control to his reluctant son."
                },
                {
                  "id": 103,
                  "name": "The Dark Knight (103)",
                  "genres": [
                    "Action",
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 152,
                  "rating": 8.9,
                  "year": 2011,
                  "imageUrl": "https://picsum.photos/300/450?random=103",
                  "description": "Batman faces the Joker, a criminal mastermind who plunges Gotham into chaos."
                },
                {
                  "id": 104,
                  "name": "Pulp Fiction (104)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 154,
                  "rating": 8.7,
                  "year": 1998,
                  "imageUrl": "https://picsum.photos/300/450?random=104",
                  "description": "Interconnected stories of crime, redemption, and violence in Los Angeles."
                },
                {
                  "id": 105,
                  "name": "Forrest Gump (105)",
                  "genres": [
                    "Drama",
                    "Romance"
                  ],
                  "durationMinutes": 142,
                  "rating": 8.8,
                  "year": 1999,
                  "imageUrl": "https://picsum.photos/300/450?random=105",
                  "description": "The life journey of a simple man with a big heart through historic events."
                },
                {
                  "id": 106,
                  "name": "Inception (106)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 148,
                  "rating": 8.7,
                  "year": 2016,
                  "imageUrl": "https://picsum.photos/300/450?random=106",
                  "description": "A skilled thief steals information by infiltrating dreams within dreams."
                },
                {
                  "id": 107,
                  "name": "Fight Club (107)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 139,
                  "rating": 8.6,
                  "year": 2006,
                  "imageUrl": "https://picsum.photos/300/450?random=107",
                  "description": "An insomniac office worker forms an underground fight club that spirals out of control."
                },
                {
                  "id": 108,
                  "name": "Interstellar (108)",
                  "genres": [
                    "Sci-Fi",
                    "Drama"
                  ],
                  "durationMinutes": 169,
                  "rating": 8.7,
                  "year": 2022,
                  "imageUrl": "https://picsum.photos/300/450?random=108",
                  "description": "Explorers travel through a wormhole in space to save humanity."
                },
                {
                  "id": 109,
                  "name": "The Matrix (109)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 136,
                  "rating": 8.6,
                  "year": 2008,
                  "imageUrl": "https://picsum.photos/300/450?random=109",
                  "description": "A hacker learns the truth about reality and his role in the war against its controllers."
                },
                {
                  "id": 110,
                  "name": "Gladiator (110)",
                  "genres": [
                    "Action",
                    "Drama"
                  ],
                  "durationMinutes": 155,
                  "rating": 8.3,
                  "year": 2000,
                  "imageUrl": "https://picsum.photos/300/450?random=110",
                  "description": "A betrayed Roman general seeks revenge against a corrupt emperor."
                },
                {
                  "id": 111,
                  "name": "The Shawshank Redemption (111)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 142,
                  "rating": 9.3,
                  "year": 1995,
                  "imageUrl": "https://picsum.photos/300/450?random=111",
                  "description": "Two imprisoned men bond over years, finding solace and redemption through acts of common decency."
                },
                {
                  "id": 112,
                  "name": "The Godfather (112)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 175,
                  "rating": 9.1,
                  "year": 1974,
                  "imageUrl": "https://picsum.photos/300/450?random=112",
                  "description": "The aging patriarch of an organized crime dynasty transfers control to his reluctant son."
                },
                {
                  "id": 113,
                  "name": "The Dark Knight (113)",
                  "genres": [
                    "Action",
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 152,
                  "rating": 8.8,
                  "year": 2011,
                  "imageUrl": "https://picsum.photos/300/450?random=113",
                  "description": "Batman faces the Joker, a criminal mastermind who plunges Gotham into chaos."
                },
                {
                  "id": 114,
                  "name": "Pulp Fiction (114)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 154,
                  "rating": 8.9,
                  "year": 1998,
                  "imageUrl": "https://picsum.photos/300/450?random=114",
                  "description": "Interconnected stories of crime, redemption, and violence in Los Angeles."
                },
                {
                  "id": 115,
                  "name": "Forrest Gump (115)",
                  "genres": [
                    "Drama",
                    "Romance"
                  ],
                  "durationMinutes": 142,
                  "rating": 8.7,
                  "year": 1999,
                  "imageUrl": "https://picsum.photos/300/450?random=115",
                  "description": "The life journey of a simple man with a big heart through historic events."
                },
                {
                  "id": 116,
                  "name": "Inception (116)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 148,
                  "rating": 8.6,
                  "year": 2016,
                  "imageUrl": "https://picsum.photos/300/450?random=116",
                  "description": "A skilled thief steals information by infiltrating dreams within dreams."
                },
                {
                  "id": 117,
                  "name": "Fight Club (117)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 139,
                  "rating": 8.8,
                  "year": 2006,
                  "imageUrl": "https://picsum.photos/300/450?random=117",
                  "description": "An insomniac office worker forms an underground fight club that spirals out of control."
                },
                {
                  "id": 118,
                  "name": "Interstellar (118)",
                  "genres": [
                    "Sci-Fi",
                    "Drama"
                  ],
                  "durationMinutes": 169,
                  "rating": 8.6,
                  "year": 2022,
                  "imageUrl": "https://picsum.photos/300/450?random=118",
                  "description": "Explorers travel through a wormhole in space to save humanity."
                },
                {
                  "id": 119,
                  "name": "The Matrix (119)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 136,
                  "rating": 8.5,
                  "year": 2008,
                  "imageUrl": "https://picsum.photos/300/450?random=119",
                  "description": "A hacker learns the truth about reality and his role in the war against its controllers."
                },
                {
                  "id": 120,
                  "name": "Gladiator (120)",
                  "genres": [
                    "Action",
                    "Drama"
                  ],
                  "durationMinutes": 155,
                  "rating": 8.5,
                  "year": 2000,
                  "imageUrl": "https://picsum.photos/300/450?random=120",
                  "description": "A betrayed Roman general seeks revenge against a corrupt emperor."
                },
                {
                  "id": 121,
                  "name": "The Shawshank Redemption (121)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 142,
                  "rating": 9.2,
                  "year": 1995,
                  "imageUrl": "https://picsum.photos/300/450?random=121",
                  "description": "Two imprisoned men bond over years, finding solace and redemption through acts of common decency."
                },
                {
                  "id": 122,
                  "name": "The Godfather (122)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 175,
                  "rating": 9.0,
                  "year": 1974,
                  "imageUrl": "https://picsum.photos/300/450?random=122",
                  "description": "The aging patriarch of an organized crime dynasty transfers control to his reluctant son."
                },
                {
                  "id": 123,
                  "name": "The Dark Knight (123)",
                  "genres": [
                    "Action",
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 152,
                  "rating": 9.0,
                  "year": 2011,
                  "imageUrl": "https://picsum.photos/300/450?random=123",
                  "description": "Batman faces the Joker, a criminal mastermind who plunges Gotham into chaos."
                },
                {
                  "id": 124,
                  "name": "Pulp Fiction (124)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 154,
                  "rating": 8.8,
                  "year": 1998,
                  "imageUrl": "https://picsum.photos/300/450?random=124",
                  "description": "Interconnected stories of crime, redemption, and violence in Los Angeles."
                },
                {
                  "id": 125,
                  "name": "Forrest Gump (125)",
                  "genres": [
                    "Drama",
                    "Romance"
                  ],
                  "durationMinutes": 142,
                  "rating": 8.6,
                  "year": 1999,
                  "imageUrl": "https://picsum.photos/300/450?random=125",
                  "description": "The life journey of a simple man with a big heart through historic events."
                },
                {
                  "id": 126,
                  "name": "Inception (126)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 148,
                  "rating": 8.8,
                  "year": 2016,
                  "imageUrl": "https://picsum.photos/300/450?random=126",
                  "description": "A skilled thief steals information by infiltrating dreams within dreams."
                },
                {
                  "id": 127,
                  "name": "Fight Club (127)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 139,
                  "rating": 8.7,
                  "year": 2006,
                  "imageUrl": "https://picsum.photos/300/450?random=127",
                  "description": "An insomniac office worker forms an underground fight club that spirals out of control."
                },
                {
                  "id": 128,
                  "name": "Interstellar (128)",
                  "genres": [
                    "Sci-Fi",
                    "Drama"
                  ],
                  "durationMinutes": 169,
                  "rating": 8.5,
                  "year": 2022,
                  "imageUrl": "https://picsum.photos/300/450?random=128",
                  "description": "Explorers travel through a wormhole in space to save humanity."
                },
                {
                  "id": 129,
                  "name": "The Matrix (129)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 136,
                  "rating": 8.7,
                  "year": 2008,
                  "imageUrl": "https://picsum.photos/300/450?random=129",
                  "description": "A hacker learns the truth about reality and his role in the war against its controllers."
                },
                {
                  "id": 130,
                  "name": "Gladiator (130)",
                  "genres": [
                    "Action",
                    "Drama"
                  ],
                  "durationMinutes": 155,
                  "rating": 8.4,
                  "year": 2000,
                  "imageUrl": "https://picsum.photos/300/450?random=130",
                  "description": "A betrayed Roman general seeks revenge against a corrupt emperor."
                },
                {
                  "id": 131,
                  "name": "The Shawshank Redemption (131)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 142,
                  "rating": 9.1,
                  "year": 1995,
                  "imageUrl": "https://picsum.photos/300/450?random=131",
                  "description": "Two imprisoned men bond over years, finding solace and redemption through acts of common decency."
                },
                {
                  "id": 132,
                  "name": "The Godfather (132)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 175,
                  "rating": 9.2,
                  "year": 1974,
                  "imageUrl": "https://picsum.photos/300/450?random=132",
                  "description": "The aging patriarch of an organized crime dynasty transfers control to his reluctant son."
                },
                {
                  "id": 133,
                  "name": "The Dark Knight (133)",
                  "genres": [
                    "Action",
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 152,
                  "rating": 8.9,
                  "year": 2011,
                  "imageUrl": "https://picsum.photos/300/450?random=133",
                  "description": "Batman faces the Joker, a criminal mastermind who plunges Gotham into chaos."
                },
                {
                  "id": 134,
                  "name": "Pulp Fiction (134)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 154,
                  "rating": 8.7,
                  "year": 1998,
                  "imageUrl": "https://picsum.photos/300/450?random=134",
                  "description": "Interconnected stories of crime, redemption, and violence in Los Angeles."
                },
                {
                  "id": 135,
                  "name": "Forrest Gump (135)",
                  "genres": [
                    "Drama",
                    "Romance"
                  ],
                  "durationMinutes": 142,
                  "rating": 8.8,
                  "year": 1999,
                  "imageUrl": "https://picsum.photos/300/450?random=135",
                  "description": "The life journey of a simple man with a big heart through historic events."
                },
                {
                  "id": 136,
                  "name": "Inception (136)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 148,
                  "rating": 8.7,
                  "year": 2016,
                  "imageUrl": "https://picsum.photos/300/450?random=136",
                  "description": "A skilled thief steals information by infiltrating dreams within dreams."
                },
                {
                  "id": 137,
                  "name": "Fight Club (137)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 139,
                  "rating": 8.6,
                  "year": 2006,
                  "imageUrl": "https://picsum.photos/300/450?random=137",
                  "description": "An insomniac office worker forms an underground fight club that spirals out of control."
                },
                {
                  "id": 138,
                  "name": "Interstellar (138)",
                  "genres": [
                    "Sci-Fi",
                    "Drama"
                  ],
                  "durationMinutes": 169,
                  "rating": 8.7,
                  "year": 2022,
                  "imageUrl": "https://picsum.photos/300/450?random=138",
                  "description": "Explorers travel through a wormhole in space to save humanity."
                },
                {
                  "id": 139,
                  "name": "The Matrix (139)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 136,
                  "rating": 8.6,
                  "year": 2008,
                  "imageUrl": "https://picsum.photos/300/450?random=139",
                  "description": "A hacker learns the truth about reality and his role in the war against its controllers."
                },
                {
                  "id": 140,
                  "name": "Gladiator (140)",
                  "genres": [
                    "Action",
                    "Drama"
                  ],
                  "durationMinutes": 155,
                  "rating": 8.3,
                  "year": 2000,
                  "imageUrl": "https://picsum.photos/300/450?random=140",
                  "description": "A betrayed Roman general seeks revenge against a corrupt emperor."
                },
                {
                  "id": 141,
                  "name": "The Shawshank Redemption (141)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 142,
                  "rating": 9.3,
                  "year": 1995,
                  "imageUrl": "https://picsum.photos/300/450?random=141",
                  "description": "Two imprisoned men bond over years, finding solace and redemption through acts of common decency."
                },
                {
                  "id": 142,
                  "name": "The Godfather (142)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 175,
                  "rating": 9.1,
                  "year": 1974,
                  "imageUrl": "https://picsum.photos/300/450?random=142",
                  "description": "The aging patriarch of an organized crime dynasty transfers control to his reluctant son."
                },
                {
                  "id": 143,
                  "name": "The Dark Knight (143)",
                  "genres": [
                    "Action",
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 152,
                  "rating": 8.8,
                  "year": 2011,
                  "imageUrl": "https://picsum.photos/300/450?random=143",
                  "description": "Batman faces the Joker, a criminal mastermind who plunges Gotham into chaos."
                },
                {
                  "id": 144,
                  "name": "Pulp Fiction (144)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 154,
                  "rating": 8.9,
                  "year": 1998,
                  "imageUrl": "https://picsum.photos/300/450?random=144",
                  "description": "Interconnected stories of crime, redemption, and violence in Los Angeles."
                },
                {
                  "id": 145,
                  "name": "Forrest Gump (145)",
                  "genres": [
                    "Drama",
                    "Romance"
                  ],
                  "durationMinutes": 142,
                  "rating": 8.7,
                  "year": 1999,
                  "imageUrl": "https://picsum.photos/300/450?random=145",
                  "description": "The life journey of a simple man with a big heart through historic events."
                },
                {
                  "id": 146,
                  "name": "Inception (146)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 148,
                  "rating": 8.6,
                  "year": 2016,
                  "imageUrl": "https://picsum.photos/300/450?random=146",
                  "description": "A skilled thief steals information by infiltrating dreams within dreams."
                },
                {
                  "id": 147,
                  "name": "Fight Club (147)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 139,
                  "rating": 8.8,
                  "year": 2006,
                  "imageUrl": "https://picsum.photos/300/450?random=147",
                  "description": "An insomniac office worker forms an underground fight club that spirals out of control."
                },
                {
                  "id": 148,
                  "name": "Interstellar (148)",
                  "genres": [
                    "Sci-Fi",
                    "Drama"
                  ],
                  "durationMinutes": 169,
                  "rating": 8.6,
                  "year": 2022,
                  "imageUrl": "https://picsum.photos/300/450?random=148",
                  "description": "Explorers travel through a wormhole in space to save humanity."
                },
                {
                  "id": 149,
                  "name": "The Matrix (149)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 136,
                  "rating": 8.5,
                  "year": 2008,
                  "imageUrl": "https://picsum.photos/300/450?random=149",
                  "description": "A hacker learns the truth about reality and his role in the war against its controllers."
                },
                {
                  "id": 150,
                  "name": "Gladiator (150)",
                  "genres": [
                    "Action",
                    "Drama"
                  ],
                  "durationMinutes": 155,
                  "rating": 8.5,
                  "year": 2000,
                  "imageUrl": "https://picsum.photos/300/450?random=150",
                  "description": "A betrayed Roman general seeks revenge against a corrupt emperor."
                },
                {
                  "id": 151,
                  "name": "The Shawshank Redemption (151)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 142,
                  "rating": 9.2,
                  "year": 1995,
                  "imageUrl": "https://picsum.photos/300/450?random=151",
                  "description": "Two imprisoned men bond over years, finding solace and redemption through acts of common decency."
                },
                {
                  "id": 152,
                  "name": "The Godfather (152)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 175,
                  "rating": 9.0,
                  "year": 1974,
                  "imageUrl": "https://picsum.photos/300/450?random=152",
                  "description": "The aging patriarch of an organized crime dynasty transfers control to his reluctant son."
                },
                {
                  "id": 153,
                  "name": "The Dark Knight (153)",
                  "genres": [
                    "Action",
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 152,
                  "rating": 9.0,
                  "year": 2011,
                  "imageUrl": "https://picsum.photos/300/450?random=153",
                  "description": "Batman faces the Joker, a criminal mastermind who plunges Gotham into chaos."
                },
                {
                  "id": 154,
                  "name": "Pulp Fiction (154)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 154,
                  "rating": 8.8,
                  "year": 1998,
                  "imageUrl": "https://picsum.photos/300/450?random=154",
                  "description": "Interconnected stories of crime, redemption, and violence in Los Angeles."
                },
                {
                  "id": 155,
                  "name": "Forrest Gump (155)",
                  "genres": [
                    "Drama",
                    "Romance"
                  ],
                  "durationMinutes": 142,
                  "rating": 8.6,
                  "year": 1999,
                  "imageUrl": "https://picsum.photos/300/450?random=155",
                  "description": "The life journey of a simple man with a big heart through historic events."
                },
                {
                  "id": 156,
                  "name": "Inception (156)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 148,
                  "rating": 8.8,
                  "year": 2016,
                  "imageUrl": "https://picsum.photos/300/450?random=156",
                  "description": "A skilled thief steals information by infiltrating dreams within dreams."
                },
                {
                  "id": 157,
                  "name": "Fight Club (157)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 139,
                  "rating": 8.7,
                  "year": 2006,
                  "imageUrl": "https://picsum.photos/300/450?random=157",
                  "description": "An insomniac office worker forms an underground fight club that spirals out of control."
                },
                {
                  "id": 158,
                  "name": "Interstellar (158)",
                  "genres": [
                    "Sci-Fi",
                    "Drama"
                  ],
                  "durationMinutes": 169,
                  "rating": 8.5,
                  "year": 2022,
                  "imageUrl": "https://picsum.photos/300/450?random=158",
                  "description": "Explorers travel through a wormhole in space to save humanity."
                },
                {
                  "id": 159,
                  "name": "The Matrix (159)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 136,
                  "rating": 8.7,
                  "year": 2008,
                  "imageUrl": "https://picsum.photos/300/450?random=159",
                  "description": "A hacker learns the truth about reality and his role in the war against its controllers."
                },
                {
                  "id": 160,
                  "name": "Gladiator (160)",
                  "genres": [
                    "Action",
                    "Drama"
                  ],
                  "durationMinutes": 155,
                  "rating": 8.4,
                  "year": 2000,
                  "imageUrl": "https://picsum.photos/300/450?random=160",
                  "description": "A betrayed Roman general seeks revenge against a corrupt emperor."
                },
                {
                  "id": 161,
                  "name": "The Shawshank Redemption (161)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 142,
                  "rating": 9.1,
                  "year": 1995,
                  "imageUrl": "https://picsum.photos/300/450?random=161",
                  "description": "Two imprisoned men bond over years, finding solace and redemption through acts of common decency."
                },
                {
                  "id": 162,
                  "name": "The Godfather (162)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 175,
                  "rating": 9.2,
                  "year": 1974,
                  "imageUrl": "https://picsum.photos/300/450?random=162",
                  "description": "The aging patriarch of an organized crime dynasty transfers control to his reluctant son."
                },
                {
                  "id": 163,
                  "name": "The Dark Knight (163)",
                  "genres": [
                    "Action",
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 152,
                  "rating": 8.9,
                  "year": 2011,
                  "imageUrl": "https://picsum.photos/300/450?random=163",
                  "description": "Batman faces the Joker, a criminal mastermind who plunges Gotham into chaos."
                },
                {
                  "id": 164,
                  "name": "Pulp Fiction (164)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 154,
                  "rating": 8.7,
                  "year": 1998,
                  "imageUrl": "https://picsum.photos/300/450?random=164",
                  "description": "Interconnected stories of crime, redemption, and violence in Los Angeles."
                },
                {
                  "id": 165,
                  "name": "Forrest Gump (165)",
                  "genres": [
                    "Drama",
                    "Romance"
                  ],
                  "durationMinutes": 142,
                  "rating": 8.8,
                  "year": 1999,
                  "imageUrl": "https://picsum.photos/300/450?random=165",
                  "description": "The life journey of a simple man with a big heart through historic events."
                },
                {
                  "id": 166,
                  "name": "Inception (166)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 148,
                  "rating": 8.7,
                  "year": 2016,
                  "imageUrl": "https://picsum.photos/300/450?random=166",
                  "description": "A skilled thief steals information by infiltrating dreams within dreams."
                },
                {
                  "id": 167,
                  "name": "Fight Club (167)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 139,
                  "rating": 8.6,
                  "year": 2006,
                  "imageUrl": "https://picsum.photos/300/450?random=167",
                  "description": "An insomniac office worker forms an underground fight club that spirals out of control."
                },
                {
                  "id": 168,
                  "name": "Interstellar (168)",
                  "genres": [
                    "Sci-Fi",
                    "Drama"
                  ],
                  "durationMinutes": 169,
                  "rating": 8.7,
                  "year": 2022,
                  "imageUrl": "https://picsum.photos/300/450?random=168",
                  "description": "Explorers travel through a wormhole in space to save humanity."
                },
                {
                  "id": 169,
                  "name": "The Matrix (169)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 136,
                  "rating": 8.6,
                  "year": 2008,
                  "imageUrl": "https://picsum.photos/300/450?random=169",
                  "description": "A hacker learns the truth about reality and his role in the war against its controllers."
                },
                {
                  "id": 170,
                  "name": "Gladiator (170)",
                  "genres": [
                    "Action",
                    "Drama"
                  ],
                  "durationMinutes": 155,
                  "rating": 8.3,
                  "year": 2000,
                  "imageUrl": "https://picsum.photos/300/450?random=170",
                  "description": "A betrayed Roman general seeks revenge against a corrupt emperor."
                },
                {
                  "id": 171,
                  "name": "The Shawshank Redemption (171)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 142,
                  "rating": 9.3,
                  "year": 1995,
                  "imageUrl": "https://picsum.photos/300/450?random=171",
                  "description": "Two imprisoned men bond over years, finding solace and redemption through acts of common decency."
                },
                {
                  "id": 172,
                  "name": "The Godfather (172)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 175,
                  "rating": 9.1,
                  "year": 1974,
                  "imageUrl": "https://picsum.photos/300/450?random=172",
                  "description": "The aging patriarch of an organized crime dynasty transfers control to his reluctant son."
                },
                {
                  "id": 173,
                  "name": "The Dark Knight (173)",
                  "genres": [
                    "Action",
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 152,
                  "rating": 8.8,
                  "year": 2011,
                  "imageUrl": "https://picsum.photos/300/450?random=173",
                  "description": "Batman faces the Joker, a criminal mastermind who plunges Gotham into chaos."
                },
                {
                  "id": 174,
                  "name": "Pulp Fiction (174)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 154,
                  "rating": 8.9,
                  "year": 1998,
                  "imageUrl": "https://picsum.photos/300/450?random=174",
                  "description": "Interconnected stories of crime, redemption, and violence in Los Angeles."
                },
                {
                  "id": 175,
                  "name": "Forrest Gump (175)",
                  "genres": [
                    "Drama",
                    "Romance"
                  ],
                  "durationMinutes": 142,
                  "rating": 8.7,
                  "year": 1999,
                  "imageUrl": "https://picsum.photos/300/450?random=175",
                  "description": "The life journey of a simple man with a big heart through historic events."
                },
                {
                  "id": 176,
                  "name": "Inception (176)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 148,
                  "rating": 8.6,
                  "year": 2016,
                  "imageUrl": "https://picsum.photos/300/450?random=176",
                  "description": "A skilled thief steals information by infiltrating dreams within dreams."
                },
                {
                  "id": 177,
                  "name": "Fight Club (177)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 139,
                  "rating": 8.8,
                  "year": 2006,
                  "imageUrl": "https://picsum.photos/300/450?random=177",
                  "description": "An insomniac office worker forms an underground fight club that spirals out of control."
                },
                {
                  "id": 178,
                  "name": "Interstellar (178)",
                  "genres": [
                    "Sci-Fi",
                    "Drama"
                  ],
                  "durationMinutes": 169,
                  "rating": 8.6,
                  "year": 2022,
                  "imageUrl": "https://picsum.photos/300/450?random=178",
                  "description": "Explorers travel through a wormhole in space to save humanity."
                },
                {
                  "id": 179,
                  "name": "The Matrix (179)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 136,
                  "rating": 8.5,
                  "year": 2008,
                  "imageUrl": "https://picsum.photos/300/450?random=179",
                  "description": "A hacker learns the truth about reality and his role in the war against its controllers."
                },
                {
                  "id": 180,
                  "name": "Gladiator (180)",
                  "genres": [
                    "Action",
                    "Drama"
                  ],
                  "durationMinutes": 155,
                  "rating": 8.5,
                  "year": 2000,
                  "imageUrl": "https://picsum.photos/300/450?random=180",
                  "description": "A betrayed Roman general seeks revenge against a corrupt emperor."
                },
                {
                  "id": 181,
                  "name": "The Shawshank Redemption (181)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 142,
                  "rating": 9.2,
                  "year": 1995,
                  "imageUrl": "https://picsum.photos/300/450?random=181",
                  "description": "Two imprisoned men bond over years, finding solace and redemption through acts of common decency."
                },
                {
                  "id": 182,
                  "name": "The Godfather (182)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 175,
                  "rating": 9.0,
                  "year": 1974,
                  "imageUrl": "https://picsum.photos/300/450?random=182",
                  "description": "The aging patriarch of an organized crime dynasty transfers control to his reluctant son."
                },
                {
                  "id": 183,
                  "name": "The Dark Knight (183)",
                  "genres": [
                    "Action",
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 152,
                  "rating": 9.0,
                  "year": 2011,
                  "imageUrl": "https://picsum.photos/300/450?random=183",
                  "description": "Batman faces the Joker, a criminal mastermind who plunges Gotham into chaos."
                },
                {
                  "id": 184,
                  "name": "Pulp Fiction (184)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 154,
                  "rating": 8.8,
                  "year": 1998,
                  "imageUrl": "https://picsum.photos/300/450?random=184",
                  "description": "Interconnected stories of crime, redemption, and violence in Los Angeles."
                },
                {
                  "id": 185,
                  "name": "Forrest Gump (185)",
                  "genres": [
                    "Drama",
                    "Romance"
                  ],
                  "durationMinutes": 142,
                  "rating": 8.6,
                  "year": 1999,
                  "imageUrl": "https://picsum.photos/300/450?random=185",
                  "description": "The life journey of a simple man with a big heart through historic events."
                },
                {
                  "id": 186,
                  "name": "Inception (186)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 148,
                  "rating": 8.8,
                  "year": 2016,
                  "imageUrl": "https://picsum.photos/300/450?random=186",
                  "description": "A skilled thief steals information by infiltrating dreams within dreams."
                },
                {
                  "id": 187,
                  "name": "Fight Club (187)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 139,
                  "rating": 8.7,
                  "year": 2006,
                  "imageUrl": "https://picsum.photos/300/450?random=187",
                  "description": "An insomniac office worker forms an underground fight club that spirals out of control."
                },
                {
                  "id": 188,
                  "name": "Interstellar (188)",
                  "genres": [
                    "Sci-Fi",
                    "Drama"
                  ],
                  "durationMinutes": 169,
                  "rating": 8.5,
                  "year": 2022,
                  "imageUrl": "https://picsum.photos/300/450?random=188",
                  "description": "Explorers travel through a wormhole in space to save humanity."
                },
                {
                  "id": 189,
                  "name": "The Matrix (189)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 136,
                  "rating": 8.7,
                  "year": 2008,
                  "imageUrl": "https://picsum.photos/300/450?random=189",
                  "description": "A hacker learns the truth about reality and his role in the war against its controllers."
                },
                {
                  "id": 190,
                  "name": "Gladiator (190)",
                  "genres": [
                    "Action",
                    "Drama"
                  ],
                  "durationMinutes": 155,
                  "rating": 8.4,
                  "year": 2000,
                  "imageUrl": "https://picsum.photos/300/450?random=190",
                  "description": "A betrayed Roman general seeks revenge against a corrupt emperor."
                },
                {
                  "id": 191,
                  "name": "The Shawshank Redemption (191)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 142,
                  "rating": 9.1,
                  "year": 1995,
                  "imageUrl": "https://picsum.photos/300/450?random=191",
                  "description": "Two imprisoned men bond over years, finding solace and redemption through acts of common decency."
                },
                {
                  "id": 192,
                  "name": "The Godfather (192)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 175,
                  "rating": 9.2,
                  "year": 1974,
                  "imageUrl": "https://picsum.photos/300/450?random=192",
                  "description": "The aging patriarch of an organized crime dynasty transfers control to his reluctant son."
                },
                {
                  "id": 193,
                  "name": "The Dark Knight (193)",
                  "genres": [
                    "Action",
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 152,
                  "rating": 8.9,
                  "year": 2011,
                  "imageUrl": "https://picsum.photos/300/450?random=193",
                  "description": "Batman faces the Joker, a criminal mastermind who plunges Gotham into chaos."
                },
                {
                  "id": 194,
                  "name": "Pulp Fiction (194)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 154,
                  "rating": 8.7,
                  "year": 1998,
                  "imageUrl": "https://picsum.photos/300/450?random=194",
                  "description": "Interconnected stories of crime, redemption, and violence in Los Angeles."
                },
                {
                  "id": 195,
                  "name": "Forrest Gump (195)",
                  "genres": [
                    "Drama",
                    "Romance"
                  ],
                  "durationMinutes": 142,
                  "rating": 8.8,
                  "year": 1999,
                  "imageUrl": "https://picsum.photos/300/450?random=195",
                  "description": "The life journey of a simple man with a big heart through historic events."
                },
                {
                  "id": 196,
                  "name": "Inception (196)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 148,
                  "rating": 8.7,
                  "year": 2016,
                  "imageUrl": "https://picsum.photos/300/450?random=196",
                  "description": "A skilled thief steals information by infiltrating dreams within dreams."
                },
                {
                  "id": 197,
                  "name": "Fight Club (197)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 139,
                  "rating": 8.6,
                  "year": 2006,
                  "imageUrl": "https://picsum.photos/300/450?random=197",
                  "description": "An insomniac office worker forms an underground fight club that spirals out of control."
                },
                {
                  "id": 198,
                  "name": "Interstellar (198)",
                  "genres": [
                    "Sci-Fi",
                    "Drama"
                  ],
                  "durationMinutes": 169,
                  "rating": 8.7,
                  "year": 2022,
                  "imageUrl": "https://picsum.photos/300/450?random=198",
                  "description": "Explorers travel through a wormhole in space to save humanity."
                },
                {
                  "id": 199,
                  "name": "The Matrix (199)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 136,
                  "rating": 8.6,
                  "year": 2008,
                  "imageUrl": "https://picsum.photos/300/450?random=199",
                  "description": "A hacker learns the truth about reality and his role in the war against its controllers."
                },
                {
                  "id": 200,
                  "name": "Gladiator (200)",
                  "genres": [
                    "Action",
                    "Drama"
                  ],
                  "durationMinutes": 155,
                  "rating": 8.3,
                  "year": 2000,
                  "imageUrl": "https://picsum.photos/300/450?random=200",
                  "description": "A betrayed Roman general seeks revenge against a corrupt emperor."
                },
                {
                  "id": 201,
                  "name": "The Shawshank Redemption (201)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 142,
                  "rating": 9.3,
                  "year": 1995,
                  "imageUrl": "https://picsum.photos/300/450?random=201",
                  "description": "Two imprisoned men bond over years, finding solace and redemption through acts of common decency."
                },
                {
                  "id": 202,
                  "name": "The Godfather (202)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 175,
                  "rating": 9.1,
                  "year": 1974,
                  "imageUrl": "https://picsum.photos/300/450?random=202",
                  "description": "The aging patriarch of an organized crime dynasty transfers control to his reluctant son."
                },
                {
                  "id": 203,
                  "name": "The Dark Knight (203)",
                  "genres": [
                    "Action",
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 152,
                  "rating": 8.8,
                  "year": 2011,
                  "imageUrl": "https://picsum.photos/300/450?random=203",
                  "description": "Batman faces the Joker, a criminal mastermind who plunges Gotham into chaos."
                },
                {
                  "id": 204,
                  "name": "Pulp Fiction (204)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 154,
                  "rating": 8.9,
                  "year": 1998,
                  "imageUrl": "https://picsum.photos/300/450?random=204",
                  "description": "Interconnected stories of crime, redemption, and violence in Los Angeles."
                },
                {
                  "id": 205,
                  "name": "Forrest Gump (205)",
                  "genres": [
                    "Drama",
                    "Romance"
                  ],
                  "durationMinutes": 142,
                  "rating": 8.7,
                  "year": 1999,
                  "imageUrl": "https://picsum.photos/300/450?random=205",
                  "description": "The life journey of a simple man with a big heart through historic events."
                },
                {
                  "id": 206,
                  "name": "Inception (206)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 148,
                  "rating": 8.6,
                  "year": 2016,
                  "imageUrl": "https://picsum.photos/300/450?random=206",
                  "description": "A skilled thief steals information by infiltrating dreams within dreams."
                },
                {
                  "id": 207,
                  "name": "Fight Club (207)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 139,
                  "rating": 8.8,
                  "year": 2006,
                  "imageUrl": "https://picsum.photos/300/450?random=207",
                  "description": "An insomniac office worker forms an underground fight club that spirals out of control."
                },
                {
                  "id": 208,
                  "name": "Interstellar (208)",
                  "genres": [
                    "Sci-Fi",
                    "Drama"
                  ],
                  "durationMinutes": 169,
                  "rating": 8.6,
                  "year": 2022,
                  "imageUrl": "https://picsum.photos/300/450?random=208",
                  "description": "Explorers travel through a wormhole in space to save humanity."
                },
                {
                  "id": 209,
                  "name": "The Matrix (209)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 136,
                  "rating": 8.5,
                  "year": 2008,
                  "imageUrl": "https://picsum.photos/300/450?random=209",
                  "description": "A hacker learns the truth about reality and his role in the war against its controllers."
                },
                {
                  "id": 210,
                  "name": "Gladiator (210)",
                  "genres": [
                    "Action",
                    "Drama"
                  ],
                  "durationMinutes": 155,
                  "rating": 8.5,
                  "year": 2000,
                  "imageUrl": "https://picsum.photos/300/450?random=210",
                  "description": "A betrayed Roman general seeks revenge against a corrupt emperor."
                },
                {
                  "id": 211,
                  "name": "The Shawshank Redemption (211)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 142,
                  "rating": 9.2,
                  "year": 1995,
                  "imageUrl": "https://picsum.photos/300/450?random=211",
                  "description": "Two imprisoned men bond over years, finding solace and redemption through acts of common decency."
                },
                {
                  "id": 212,
                  "name": "The Godfather (212)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 175,
                  "rating": 9.0,
                  "year": 1974,
                  "imageUrl": "https://picsum.photos/300/450?random=212",
                  "description": "The aging patriarch of an organized crime dynasty transfers control to his reluctant son."
                },
                {
                  "id": 213,
                  "name": "The Dark Knight (213)",
                  "genres": [
                    "Action",
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 152,
                  "rating": 9.0,
                  "year": 2011,
                  "imageUrl": "https://picsum.photos/300/450?random=213",
                  "description": "Batman faces the Joker, a criminal mastermind who plunges Gotham into chaos."
                },
                {
                  "id": 214,
                  "name": "Pulp Fiction (214)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 154,
                  "rating": 8.8,
                  "year": 1998,
                  "imageUrl": "https://picsum.photos/300/450?random=214",
                  "description": "Interconnected stories of crime, redemption, and violence in Los Angeles."
                },
                {
                  "id": 215,
                  "name": "Forrest Gump (215)",
                  "genres": [
                    "Drama",
                    "Romance"
                  ],
                  "durationMinutes": 142,
                  "rating": 8.6,
                  "year": 1999,
                  "imageUrl": "https://picsum.photos/300/450?random=215",
                  "description": "The life journey of a simple man with a big heart through historic events."
                },
                {
                  "id": 216,
                  "name": "Inception (216)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 148,
                  "rating": 8.8,
                  "year": 2016,
                  "imageUrl": "https://picsum.photos/300/450?random=216",
                  "description": "A skilled thief steals information by infiltrating dreams within dreams."
                },
                {
                  "id": 217,
                  "name": "Fight Club (217)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 139,
                  "rating": 8.7,
                  "year": 2006,
                  "imageUrl": "https://picsum.photos/300/450?random=217",
                  "description": "An insomniac office worker forms an underground fight club that spirals out of control."
                },
                {
                  "id": 218,
                  "name": "Interstellar (218)",
                  "genres": [
                    "Sci-Fi",
                    "Drama"
                  ],
                  "durationMinutes": 169,
                  "rating": 8.5,
                  "year": 2022,
                  "imageUrl": "https://picsum.photos/300/450?random=218",
                  "description": "Explorers travel through a wormhole in space to save humanity."
                },
                {
                  "id": 219,
                  "name": "The Matrix (219)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 136,
                  "rating": 8.7,
                  "year": 2008,
                  "imageUrl": "https://picsum.photos/300/450?random=219",
                  "description": "A hacker learns the truth about reality and his role in the war against its controllers."
                },
                {
                  "id": 220,
                  "name": "Gladiator (220)",
                  "genres": [
                    "Action",
                    "Drama"
                  ],
                  "durationMinutes": 155,
                  "rating": 8.4,
                  "year": 2000,
                  "imageUrl": "https://picsum.photos/300/450?random=220",
                  "description": "A betrayed Roman general seeks revenge against a corrupt emperor."
                },
                {
                  "id": 221,
                  "name": "The Shawshank Redemption (221)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 142,
                  "rating": 9.1,
                  "year": 1995,
                  "imageUrl": "https://picsum.photos/300/450?random=221",
                  "description": "Two imprisoned men bond over years, finding solace and redemption through acts of common decency."
                },
                {
                  "id": 222,
                  "name": "The Godfather (222)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 175,
                  "rating": 9.2,
                  "year": 1974,
                  "imageUrl": "https://picsum.photos/300/450?random=222",
                  "description": "The aging patriarch of an organized crime dynasty transfers control to his reluctant son."
                },
                {
                  "id": 223,
                  "name": "The Dark Knight (223)",
                  "genres": [
                    "Action",
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 152,
                  "rating": 8.9,
                  "year": 2011,
                  "imageUrl": "https://picsum.photos/300/450?random=223",
                  "description": "Batman faces the Joker, a criminal mastermind who plunges Gotham into chaos."
                },
                {
                  "id": 224,
                  "name": "Pulp Fiction (224)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 154,
                  "rating": 8.7,
                  "year": 1998,
                  "imageUrl": "https://picsum.photos/300/450?random=224",
                  "description": "Interconnected stories of crime, redemption, and violence in Los Angeles."
                },
                {
                  "id": 225,
                  "name": "Forrest Gump (225)",
                  "genres": [
                    "Drama",
                    "Romance"
                  ],
                  "durationMinutes": 142,
                  "rating": 8.8,
                  "year": 1999,
                  "imageUrl": "https://picsum.photos/300/450?random=225",
                  "description": "The life journey of a simple man with a big heart through historic events."
                },
                {
                  "id": 226,
                  "name": "Inception (226)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 148,
                  "rating": 8.7,
                  "year": 2016,
                  "imageUrl": "https://picsum.photos/300/450?random=226",
                  "description": "A skilled thief steals information by infiltrating dreams within dreams."
                },
                {
                  "id": 227,
                  "name": "Fight Club (227)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 139,
                  "rating": 8.6,
                  "year": 2006,
                  "imageUrl": "https://picsum.photos/300/450?random=227",
                  "description": "An insomniac office worker forms an underground fight club that spirals out of control."
                },
                {
                  "id": 228,
                  "name": "Interstellar (228)",
                  "genres": [
                    "Sci-Fi",
                    "Drama"
                  ],
                  "durationMinutes": 169,
                  "rating": 8.7,
                  "year": 2022,
                  "imageUrl": "https://picsum.photos/300/450?random=228",
                  "description": "Explorers travel through a wormhole in space to save humanity."
                },
                {
                  "id": 229,
                  "name": "The Matrix (229)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 136,
                  "rating": 8.6,
                  "year": 2008,
                  "imageUrl": "https://picsum.photos/300/450?random=229",
                  "description": "A hacker learns the truth about reality and his role in the war against its controllers."
                },
                {
                  "id": 230,
                  "name": "Gladiator (230)",
                  "genres": [
                    "Action",
                    "Drama"
                  ],
                  "durationMinutes": 155,
                  "rating": 8.3,
                  "year": 2000,
                  "imageUrl": "https://picsum.photos/300/450?random=230",
                  "description": "A betrayed Roman general seeks revenge against a corrupt emperor."
                },
                {
                  "id": 231,
                  "name": "The Shawshank Redemption (231)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 142,
                  "rating": 9.3,
                  "year": 1995,
                  "imageUrl": "https://picsum.photos/300/450?random=231",
                  "description": "Two imprisoned men bond over years, finding solace and redemption through acts of common decency."
                },
                {
                  "id": 232,
                  "name": "The Godfather (232)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 175,
                  "rating": 9.1,
                  "year": 1974,
                  "imageUrl": "https://picsum.photos/300/450?random=232",
                  "description": "The aging patriarch of an organized crime dynasty transfers control to his reluctant son."
                },
                {
                  "id": 233,
                  "name": "The Dark Knight (233)",
                  "genres": [
                    "Action",
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 152,
                  "rating": 8.8,
                  "year": 2011,
                  "imageUrl": "https://picsum.photos/300/450?random=233",
                  "description": "Batman faces the Joker, a criminal mastermind who plunges Gotham into chaos."
                },
                {
                  "id": 234,
                  "name": "Pulp Fiction (234)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 154,
                  "rating": 8.9,
                  "year": 1998,
                  "imageUrl": "https://picsum.photos/300/450?random=234",
                  "description": "Interconnected stories of crime, redemption, and violence in Los Angeles."
                },
                {
                  "id": 235,
                  "name": "Forrest Gump (235)",
                  "genres": [
                    "Drama",
                    "Romance"
                  ],
                  "durationMinutes": 142,
                  "rating": 8.7,
                  "year": 1999,
                  "imageUrl": "https://picsum.photos/300/450?random=235",
                  "description": "The life journey of a simple man with a big heart through historic events."
                },
                {
                  "id": 236,
                  "name": "Inception (236)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 148,
                  "rating": 8.6,
                  "year": 2016,
                  "imageUrl": "https://picsum.photos/300/450?random=236",
                  "description": "A skilled thief steals information by infiltrating dreams within dreams."
                },
                {
                  "id": 237,
                  "name": "Fight Club (237)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 139,
                  "rating": 8.8,
                  "year": 2006,
                  "imageUrl": "https://picsum.photos/300/450?random=237",
                  "description": "An insomniac office worker forms an underground fight club that spirals out of control."
                },
                {
                  "id": 238,
                  "name": "Interstellar (238)",
                  "genres": [
                    "Sci-Fi",
                    "Drama"
                  ],
                  "durationMinutes": 169,
                  "rating": 8.6,
                  "year": 2022,
                  "imageUrl": "https://picsum.photos/300/450?random=238",
                  "description": "Explorers travel through a wormhole in space to save humanity."
                },
                {
                  "id": 239,
                  "name": "The Matrix (239)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 136,
                  "rating": 8.5,
                  "year": 2008,
                  "imageUrl": "https://picsum.photos/300/450?random=239",
                  "description": "A hacker learns the truth about reality and his role in the war against its controllers."
                },
                {
                  "id": 240,
                  "name": "Gladiator (240)",
                  "genres": [
                    "Action",
                    "Drama"
                  ],
                  "durationMinutes": 155,
                  "rating": 8.5,
                  "year": 2000,
                  "imageUrl": "https://picsum.photos/300/450?random=240",
                  "description": "A betrayed Roman general seeks revenge against a corrupt emperor."
                },
                {
                  "id": 241,
                  "name": "The Shawshank Redemption (241)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 142,
                  "rating": 9.2,
                  "year": 1995,
                  "imageUrl": "https://picsum.photos/300/450?random=241",
                  "description": "Two imprisoned men bond over years, finding solace and redemption through acts of common decency."
                },
                {
                  "id": 242,
                  "name": "The Godfather (242)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 175,
                  "rating": 9.0,
                  "year": 1974,
                  "imageUrl": "https://picsum.photos/300/450?random=242",
                  "description": "The aging patriarch of an organized crime dynasty transfers control to his reluctant son."
                },
                {
                  "id": 243,
                  "name": "The Dark Knight (243)",
                  "genres": [
                    "Action",
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 152,
                  "rating": 9.0,
                  "year": 2011,
                  "imageUrl": "https://picsum.photos/300/450?random=243",
                  "description": "Batman faces the Joker, a criminal mastermind who plunges Gotham into chaos."
                },
                {
                  "id": 244,
                  "name": "Pulp Fiction (244)",
                  "genres": [
                    "Crime",
                    "Drama"
                  ],
                  "durationMinutes": 154,
                  "rating": 8.8,
                  "year": 1998,
                  "imageUrl": "https://picsum.photos/300/450?random=244",
                  "description": "Interconnected stories of crime, redemption, and violence in Los Angeles."
                },
                {
                  "id": 245,
                  "name": "Forrest Gump (245)",
                  "genres": [
                    "Drama",
                    "Romance"
                  ],
                  "durationMinutes": 142,
                  "rating": 8.6,
                  "year": 1999,
                  "imageUrl": "https://picsum.photos/300/450?random=245",
                  "description": "The life journey of a simple man with a big heart through historic events."
                },
                {
                  "id": 246,
                  "name": "Inception (246)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 148,
                  "rating": 8.8,
                  "year": 2016,
                  "imageUrl": "https://picsum.photos/300/450?random=246",
                  "description": "A skilled thief steals information by infiltrating dreams within dreams."
                },
                {
                  "id": 247,
                  "name": "Fight Club (247)",
                  "genres": [
                    "Drama"
                  ],
                  "durationMinutes": 139,
                  "rating": 8.7,
                  "year": 2006,
                  "imageUrl": "https://picsum.photos/300/450?random=247",
                  "description": "An insomniac office worker forms an underground fight club that spirals out of control."
                },
                {
                  "id": 248,
                  "name": "Interstellar (248)",
                  "genres": [
                    "Sci-Fi",
                    "Drama"
                  ],
                  "durationMinutes": 169,
                  "rating": 8.5,
                  "year": 2022,
                  "imageUrl": "https://picsum.photos/300/450?random=248",
                  "description": "Explorers travel through a wormhole in space to save humanity."
                },
                {
                  "id": 249,
                  "name": "The Matrix (249)",
                  "genres": [
                    "Sci-Fi",
                    "Action"
                  ],
                  "durationMinutes": 136,
                  "rating": 8.7,
                  "year": 2008,
                  "imageUrl": "https://picsum.photos/300/450?random=249",
                  "description": "A hacker learns the truth about reality and his role in the war against its controllers."
                },
                {
                  "id": 250,
                  "name": "Gladiator (250)",
                  "genres": [
                    "Action",
                    "Drama"
                  ],
                  "durationMinutes": 155,
                  "rating": 8.4,
                  "year": 2000,
                  "imageUrl": "https://picsum.photos/300/450?random=250",
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
