package com.labs.systemdesignandroid.utils

import kotlin.random.Random


fun ClosedFloatingPointRange<Float>.random(): Float {
    return Random.nextFloat() * (endInclusive - start) + start
}
