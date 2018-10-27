package classifier

import utils.inc
import kotlin.math.ln

class ReviewClassifier {
    // кол-во раз, когда слово отнесено к какому-то классу - map[слово][класс] = кол-во
    private val countOfWordAddsToType = mutableMapOf<String, MutableMap<String, Int>>()
    // общее кол-во рецензий, отнесённых к какому-то классу - map[класс] = кол-во
    private val countOfReviewsAddedToType = mutableMapOf("good" to 0, "bad" to 0, "neutral" to 0)
    // кол-во слов, отнесённых к какому-то классу - map[класс] = кол-во
    private val countOfWordsAddedToType = mutableMapOf("good" to 0, "bad" to 0, "neutral" to 0)
    // общее кол-во рецензий
    private var reviewsTotalCount = 0

    private val types = arrayOf("good", "bad", "neutral")

    fun train(review: Iterable<String>, type: String) {
        reviewsTotalCount++
        countOfReviewsAddedToType.inc(type)

        review.forEach { word ->
            if (!countOfWordAddsToType.containsKey(word)) {
                countOfWordAddsToType[word] = mutableMapOf()
            }
            if (!countOfWordAddsToType[word]!!.containsKey(type)) {
                types.forEach {
                    countOfWordAddsToType[word]!![it] = 0
                }
            }

            countOfWordAddsToType[word]?.inc(type)
            countOfWordsAddedToType.inc(type)
        }
    }

    fun train(reviews: Iterable<Pair<Iterable<String>, String>>) {
        reviews.forEach {
            train(it.first, it.second)
        }
    }

    fun classify(review: Iterable<String>): Pair<String, Double> {
        var score = Double.NEGATIVE_INFINITY
        var prediction = ""

        types.forEach { type ->
            val probability = getProbability(review, type)

            if (score < probability) {
                score = probability
                prediction = type
            }
        }

        return prediction to score
    }

    private fun getProbability(review: Iterable<String>, type: String): Double {
        val totalWordCount = countOfWordAddsToType.keys.size
        var result = ln(countOfReviewsAddedToType[type]!!.toDouble() / reviewsTotalCount)

        review.forEach { word ->
            val wordAddsCount = if (countOfWordAddsToType.containsKey(word)) {
                countOfWordAddsToType[word]!![type]!!
            } else 0

            val numerator = (1 + wordAddsCount).toDouble()
            val denominator = totalWordCount + countOfWordsAddedToType[type]!!
            result += ln(numerator / denominator)
        }

        return result
    }

}
