package com.labs.systemdesignandroid.feature.sync.useCase

import com.labs.systemdesignandroid.data.local.MovieLocalDataSource
import com.labs.systemdesignandroid.data.local.UserMovieStateEntity
import com.labs.systemdesignandroid.data.remote.UserMovieStateRemoteDataSource
import com.labs.systemdesignandroid.feature.authentication.AuthUserProvider
import javax.inject.Inject

class PullRemoteUserStateUseCase @Inject constructor(
    private val auth: AuthUserProvider,
    private val remote: UserMovieStateRemoteDataSource,
    private val local: MovieLocalDataSource
) {
    suspend operator fun invoke() {
        val uid = auth.uidOrNull() ?: return

        val remoteMap = remote.fetchAll(uid)
        for ((movieId, r) in remoteMap) {
            val localState = local.getUserState(userId = uid, movieId)

            // if local has pending changes, don't overwrite
            if (localState?.pendingSync == true) continue

            if (localState == null) {
                local.upsertUserState(
                    UserMovieStateEntity(
                        userId = uid,
                        movieId = movieId,
                        isFavorite = r.favorite,
                        isInWatchlist = r.watchlist,
                        userRating = r.rating,
                        pendingSync = false,
                        remoteUpdatedAt = r.updatedAtMillis
                    )
                )
            } else if (r.updatedAtMillis > localState.remoteUpdatedAt && r.updatedAtMillis > 0L) {
                local.applyRemoteState(
                    userId = uid,
                    movieId = movieId,
                    favorite = r.favorite,
                    watchlist = r.watchlist,
                    rating = r.rating,
                    remoteUpdatedAt = r.updatedAtMillis,
                    reactions = r.reactions,
                )
            }
        }
    }
}
