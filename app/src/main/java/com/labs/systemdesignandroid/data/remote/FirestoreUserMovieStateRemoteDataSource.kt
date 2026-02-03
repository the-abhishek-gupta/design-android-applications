package com.labs.systemdesignandroid.data.remote

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FirestoreUserMovieStateRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserMovieStateRemoteDataSource {

    private fun col(uid: String) =
        firestore.collection("users").document(uid).collection("movie_state")

    override suspend fun upsert(
        uid: String, movieId: Int, favorite: Boolean, watchlist: Boolean, rating: Int
    ) {
        col(uid).document(movieId.toString()).set(
            mapOf(
                "favorite" to favorite,
                "watchlist" to watchlist,
                "rating" to rating.coerceIn(0, 5),
                "updatedAt" to FieldValue.serverTimestamp()
            ), SetOptions.merge()
        ).await()
    }

    override suspend fun get(uid: String, movieId: Int): UserMovieStateRemote? {
        val doc = col(uid).document(movieId.toString()).get().await()
        if (!doc.exists()) return null
        return doc.toRemoteOrNull()
    }

    override fun listenAll(uid: String): Flow<Map<Int, UserMovieStateRemote>> = callbackFlow {
        val reg = col(uid).addSnapshotListener { snap, err ->
            if (err != null) {
                // logout / auth change can cause permission denied -> ignore
                // You can optionally log it:
                // Log.d("Firestore", "listenAll error: ${err.code}")
                return@addSnapshotListener
            }
            if (snap == null) return@addSnapshotListener

            val map = snap.documents.mapNotNull { doc ->
                val movieId = doc.id.toIntOrNull() ?: return@mapNotNull null
                val remote = doc.toRemoteOrNull() ?: return@mapNotNull null
                movieId to remote
            }.toMap()

            trySend(map)
        }
        awaitClose { reg.remove() }
    }.buffer(Channel.CONFLATED)

    private fun DocumentSnapshot.toRemoteOrNull(): UserMovieStateRemote? {
        val updatedAt = getTimestamp("updatedAt")?.toDate()?.time ?: 0L
        // If serverTimestamp hasn't resolved yet, updatedAt can be 0
        val fav = getBoolean("favorite") ?: false
        val watch = getBoolean("watchlist") ?: false
        val rating = (getLong("rating") ?: 0L).toInt()
        return UserMovieStateRemote(
            favorite = fav,
            watchlist = watch,
            rating = rating.coerceIn(0, 5),
            updatedAtMillis = updatedAt
        )
    }

    override suspend fun fetchAll(uid: String): Map<Int, UserMovieStateRemote> {
        val snap = col(uid).get().await()
        return snap.documents.mapNotNull { doc ->
            val movieId = doc.id.toIntOrNull() ?: return@mapNotNull null
            val remote = doc.toRemoteOrNull() ?: return@mapNotNull null
            movieId to remote
        }.toMap()
    }

}
