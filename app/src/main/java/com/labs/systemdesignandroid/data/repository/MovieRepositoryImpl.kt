package com.labs.systemdesignandroid.data.repository

import com.labs.systemdesignandroid.data.local.MovieLocalDataSource
import com.labs.systemdesignandroid.data.local.UserMovieStateEntity
import com.labs.systemdesignandroid.data.remote.MovieRemoteDataSource
import com.labs.systemdesignandroid.data.remote.UserMovieStateRemoteDataSource
import com.labs.systemdesignandroid.domain.model.MovieModel
import com.labs.systemdesignandroid.domain.repository.MovieRepository
import com.labs.systemdesignandroid.feature.authentication.AuthUserProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val local: MovieLocalDataSource,
    private val catalogRemote: MovieRemoteDataSource,
    private val userRemote: UserMovieStateRemoteDataSource,
    private val auth: AuthUserProvider
) : MovieRepository {

    override fun observeMovies(): Flow<List<MovieModel>> = local.observeMovies()

    override suspend fun refreshCatalog() {
        local.upsertCatalog(catalogRemote.fetchCatalog())
    }

    // Local writes: set pendingSync=true
    override suspend fun toggleFavorite(movieId: Int) {
        val s = local.getUserState(movieId) ?: UserMovieStateEntity(movieId = movieId)
        local.upsertUserState(s.copy(isFavorite = !s.isFavorite, pendingSync = true))
    }

    override suspend fun toggleWatchlist(movieId: Int) {
        val s = local.getUserState(movieId) ?: UserMovieStateEntity(movieId = movieId)
        local.upsertUserState(s.copy(isInWatchlist = !s.isInWatchlist, pendingSync = true))
    }

    override suspend fun rateMovie(movieId: Int, rating: Int) {
        val s = local.getUserState(movieId) ?: UserMovieStateEntity(movieId = movieId)
        local.upsertUserState(s.copy(userRating = rating.coerceIn(0, 5), pendingSync = true))
    }

    // 1) Push pending local changes to Firestore (server timestamp)
    override suspend fun pushPending() {
        val uid = auth.uidOrNull() ?: return

        val pending = local.getPendingUserState()
        for (state in pending) {
            userRemote.upsert(
                uid = uid,
                movieId = state.movieId,
                favorite = state.isFavorite,
                watchlist = state.isInWatchlist,
                rating = state.userRating
            )

            // Fetch resolved server timestamp to store remoteUpdatedAt
            // (listener will also update it, but this makes push deterministic)
            val remote = userRemote.get(uid, state.movieId)
            val resolved = remote?.updatedAtMillis ?: state.remoteUpdatedAt

            if (resolved > 0L) {
                local.markSynced(state.movieId, resolved)
            }
        }
    }

    override suspend fun syncNow() {
        val uid = auth.uidOrNull()

        refreshCatalog() // Always keep catalog fresh (works logged-out too)
        if (uid == null) return

        pullRemoteUserStateOnce(uid) //One-time pull: remote -> local

        pushPending()         //Push any local pending changes after pull
    }

    private suspend fun pullRemoteUserStateOnce(uid: String) {
        val remoteMap = userRemote.fetchAll(uid) //ensure remote has fetchAll()

        for ((movieId, remote) in remoteMap) {
            val localState = local.getUserState(movieId)

            // Don't overwrite local edits waiting to sync
            if (localState?.pendingSync == true) continue

            if (localState == null) {
                local.upsertUserState(
                    UserMovieStateEntity(
                        movieId = movieId,
                        isFavorite = remote.favorite,
                        isInWatchlist = remote.watchlist,
                        userRating = remote.rating.coerceIn(0, 5),
                        pendingSync = false,
                        remoteUpdatedAt = remote.updatedAtMillis
                    )
                )
            } else if (remote.updatedAtMillis > localState.remoteUpdatedAt && remote.updatedAtMillis > 0L) {
                local.applyRemoteState(
                    movieId = movieId,
                    favorite = remote.favorite,
                    watchlist = remote.watchlist,
                    rating = remote.rating.coerceIn(0, 5),
                    remoteUpdatedAt = remote.updatedAtMillis
                )
            }
        }
    }


    // 3) Remote listener: merge remote → local (server timestamp conflict)
    override fun observeRemoteSync(): Flow<Unit> {
        val uid = auth.uidOrNull() ?: return flowOf(Unit) // no-op when logged out

        return userRemote.listenAll(uid)
            .onEach { remoteMap ->
                remoteMap.forEach { (movieId, remote) ->
                    val localState = local.getUserState(movieId)

                    // If we don't have local state, just apply remote
                    if (localState == null) {
                        local.upsertUserState(
                            UserMovieStateEntity(
                                movieId = movieId,
                                isFavorite = remote.favorite,
                                isInWatchlist = remote.watchlist,
                                userRating = remote.rating,
                                pendingSync = false,
                                remoteUpdatedAt = remote.updatedAtMillis
                            )
                        )
                        return@forEach
                    }

                    // If local has pending changes, don't overwrite them
                    if (localState.pendingSync) return@forEach

                    // Apply remote only if server timestamp is newer
                    if (remote.updatedAtMillis > localState.remoteUpdatedAt && remote.updatedAtMillis > 0L) {
                        local.applyRemoteState(
                            movieId = movieId,
                            favorite = remote.favorite,
                            watchlist = remote.watchlist,
                            rating = remote.rating,
                            remoteUpdatedAt = remote.updatedAtMillis
                        )
                    }
                }
            }
            .map { Unit }
    }
}
