package com.labs.systemdesignandroid.feature.sync.useCase

import com.labs.systemdesignandroid.data.local.MovieLocalDataSource
import com.labs.systemdesignandroid.data.local.UserMovieStateEntity
import com.labs.systemdesignandroid.data.remote.UserMovieStateRemoteDataSource
import com.labs.systemdesignandroid.feature.authentication.AuthUserProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ObserveRemoteUserStateUseCase @Inject constructor(
    private val auth: AuthUserProvider,
    private val local: MovieLocalDataSource,
    private val remote: UserMovieStateRemoteDataSource
) {
    operator fun invoke(): Flow<Unit> {
        val uid = auth.uidOrNull() ?: return flowOf(Unit)

        return remote.listenAll(uid)
            .onEach { remoteMap ->
                for ((movieId, r) in remoteMap) {
                    val localState = local.getUserState(movieId)

                    if (localState?.pendingSync == true) continue

                    if (localState == null) {
                        local.upsertUserState(
                            UserMovieStateEntity(
                                movieId = movieId,
                                isFavorite = r.favorite,
                                isInWatchlist = r.watchlist,
                                userRating = r.rating.coerceIn(0, 5),
                                pendingSync = false,
                                remoteUpdatedAt = r.updatedAtMillis
                            )
                        )
                    } else if (r.updatedAtMillis > localState.remoteUpdatedAt && r.updatedAtMillis > 0L) {
                        local.applyRemoteState(
                            movieId = movieId,
                            favorite = r.favorite,
                            watchlist = r.watchlist,
                            rating = r.rating.coerceIn(0, 5),
                            remoteUpdatedAt = r.updatedAtMillis
                        )
                    }
                }
            }
            .map { Unit }
    }
}
