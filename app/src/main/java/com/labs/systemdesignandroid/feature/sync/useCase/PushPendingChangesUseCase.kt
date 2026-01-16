package com.labs.systemdesignandroid.feature.sync.useCase

import com.labs.systemdesignandroid.data.local.MovieLocalDataSource
import com.labs.systemdesignandroid.data.remote.UserMovieStateRemoteDataSource
import com.labs.systemdesignandroid.feature.authentication.AuthUserProvider
import javax.inject.Inject

class PushPendingChangesUseCase @Inject constructor(
    private val auth: AuthUserProvider,
    private val local: MovieLocalDataSource,
    private val remote: UserMovieStateRemoteDataSource
) {
    suspend operator fun invoke() {
        val uid = auth.uidOrNull() ?: return
        val pending = local.getPendingUserState()

        for (state in pending) {
            remote.upsert(
                uid = uid,
                movieId = state.movieId,
                favorite = state.isFavorite,
                watchlist = state.isInWatchlist,
                rating = state.userRating
            )

            // Fetch server resolved timestamp (or listener will eventually)
            val resolved = remote.get(uid, state.movieId)?.updatedAtMillis ?: 0L
            if (resolved > 0L) {
                local.markSynced(state.movieId, resolved)
            }
        }
    }
}
