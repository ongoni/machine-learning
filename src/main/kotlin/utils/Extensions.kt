package utils

import java.util.*

fun IntRange.random() = Random().nextInt((endInclusive + 1) - start) +  start

fun MutableMap<String, Int>.inc(key: String) {
    this[key] = this[key]!!.plus(1)
}