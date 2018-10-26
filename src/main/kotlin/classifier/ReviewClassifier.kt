package classifier

import utils.inc
import java.lang.Exception

class ReviewClassifier {
    // кол-во раз, когда слово отнесено к какому-то классу - map[слово][класс] = кол-во
    private val countOfWordAddsToType = mutableMapOf<String, MutableMap<String, Int>>()
    // общее кол-во рецензий, отнесённых к какому-то классу - map[класс] = кол-во
    private val countOfReviewsAddedToType = mutableMapOf("good" to 0, "bad" to 0, "neutral" to 0)
    // кол-во слов, отнесённых к какому-то классу - map[класс] = кол-во
    private val countOfWordAddedToType = mutableMapOf("good" to 0, "bad" to 0, "neutral" to 0)
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
                countOfWordAddsToType[word]!![type] = 0
            }

            countOfWordAddsToType[word]?.inc(type)
            countOfWordAddedToType.inc(type)
        }
    }

    fun train(reviews: Iterable<Pair<Iterable<String>, String>>) {
        reviews.forEach {
            train(it.first, it.second)
        }
    }

    fun classify(review: Iterable<String>): Pair<String, Double> {
        var score = 0.0
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

    private fun getTypeProbability(type: String) =
            countOfReviewsAddedToType[type]!!.toDouble() / reviewsTotalCount

    private fun getWordGivenClassProbability(word: String, type: String): Double {
        if (!countOfWordAddsToType.containsKey(word) || (countOfWordAddsToType.containsKey(word)
                        && !countOfWordAddsToType[word]!!.containsKey(type))) {
            return 1.0
        }

        var result = 1.0
        try {
            result = (countOfWordAddsToType[word]!![type]!! + 1).toDouble() /
                    countOfWordAddedToType[type]!! + getTotalWordsCount()
        } catch (ex: Exception) {
            println(ex.message)
        }

        return result
    }

    private fun getProbability(review: Iterable<String>, type: String): Double {
        var result = getTypeProbability(type)

        review.forEach { word ->
            result *= getWordGivenClassProbability(word, type)
        }

        val temp = review.fold(result) { sum, word ->
            sum * getWordGivenClassProbability(word, type)
        }

        return result
    }

    private fun getTotalWordsCount() = countOfWordAddsToType.keys.size

}
