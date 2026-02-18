package com.labs.systemdesignandroid.feature.comments.data.utils

import com.labs.systemdesignandroid.feature.comments.data.Comment
import java.util.UUID
import kotlin.math.pow
import kotlin.random.Random

fun generateProductionComments(
    rootCount: Int = 200,
    maxRepliesPerRoot: Int = 12,
    maxNestedPerReply: Int = 5,
    seed: Int? = null
): List<Comment> {

    val random = if (seed != null) Random(seed) else Random
    val comments = mutableListOf<Comment>()

    val now = System.currentTimeMillis()
    val thirtyDaysMillis = 30L * 24 * 60 * 60 * 1000

    fun randomTimestamp(): Long {
        return now - random.nextLong(0, thirtyDaysMillis)
    }

    // Power-law like distribution (few high likes, many low likes)
    fun generateLikes(max: Int): Int {
        val exponent = 2.0
        val value = (random.nextDouble().pow(exponent) * max).toInt()
        return value
    }

    fun randomUser(): String {
        return "User${random.nextInt(1000, 9999)}"
    }

    fun randomMessage(depth: Int): String {
        return listOf(
            "This movie exceeded expectations",
            " Cinematography was insane.",
            "Totally agree with this take.",
            "That scene especially stood out.",
            "Interesting perspective!"
        ).random()

    }
    fun getRandomBool() = listOf(true, false).random()

    repeat(rootCount) { rootIndex ->

        val rootId = UUID.randomUUID().toString()

        val rootComment = Comment(
            id = rootId,
            parentId = null,
            userName = randomUser(),
            message = randomMessage(0),
            isLikedByUser = getRandomBool(),
            likeCount = generateLikes(5000),
            timestamp = randomTimestamp()
        )

        comments.add(rootComment)

        val replyCount = random.nextInt(3, maxRepliesPerRoot + 1)

        repeat(replyCount) {

            val replyId = UUID.randomUUID().toString()

            val reply = Comment(
                id = replyId,
                parentId = rootId,
                userName = randomUser(),
                message = randomMessage(1),
                likeCount = generateLikes(1500),
                isLikedByUser = getRandomBool(),
                timestamp = randomTimestamp()
            )

            comments.add(reply)

            val nestedCount = random.nextInt(0, maxNestedPerReply + 1)

            repeat(nestedCount) {

                val nestedReply = Comment(
                    id = UUID.randomUUID().toString(),
                    parentId = replyId,
                    userName = randomUser(),
                    message = randomMessage(2),
                    likeCount = generateLikes(500),
                    isLikedByUser = getRandomBool(),
                    timestamp = randomTimestamp()
                )

                comments.add(nestedReply)

                // Optional 4th level (rare)
                if (random.nextFloat() < 0.2f) {
                    comments.add(
                        Comment(
                            id = UUID.randomUUID().toString(),
                            parentId = nestedReply.id,
                            userName = randomUser(),
                            message = randomMessage(3),
                            likeCount = generateLikes(200),
                            isLikedByUser = getRandomBool(),
                            timestamp = randomTimestamp()
                        )
                    )
                }
            }
        }
    }

    // Sort newest first (realistic backend behavior)
    return comments.sortedByDescending { it.timestamp }
}
