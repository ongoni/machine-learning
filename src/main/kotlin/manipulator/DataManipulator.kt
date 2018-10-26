package manipulator

import parsing.FilmReviewHandler
import parsing.FilmTopParser
import preprocessing.TextTokenHandler
import preprocessing.TextTokenizer
import utils.random
import java.io.File

object DataManipulator {

    private const val TOKEN_FOLDER = "film-reviews/tokens"

    fun parseAndSaveReviews(sleeping: Boolean = true) {
        val filmIdentifiers = FilmTopParser.getTopFilmsIdentifiers()

        filmIdentifiers.forEachIndexed { index, identifier ->
            val reviews = FilmTopParser.getFilmReviews(identifier)
            val reviewsTokens = reviews.map { TextTokenizer.getTextTokens(it) }

            FilmReviewHandler.saveReviews(identifier, reviews, index + 1)
            TextTokenHandler.saveReviewsTokens(identifier, reviewsTokens, index + 1)

            if (sleeping) {
                Thread.sleep((2..5).random() * 5000L)
            }
        }
    }

    fun getTrainingSet(): List<Pair<List<String>, String>> =
            File(TOKEN_FOLDER)
                    .listFiles()
                    .filter { x -> x.name
                            .takeWhile { it != ' ' }
                            .toInt() < 201
                    }
                    .map { fileToReview(it) }

    fun getClassifySet(): List<Pair<List<String>, String>> =
            File(TOKEN_FOLDER)
                    .listFiles()
                    .filter {
                        x -> x.name
                            .takeWhile { it != ' ' }
                            .toInt() > 200
                    }
                    .map { fileToReview(it) }

    private fun fileToReview(file: File): Pair<List<String>, String> {
        val words = file
                .readText()
                .split(", ", "[", "]")
                .toMutableList()

        val type = words
                .first { x -> x.contains(Regex("good|bad|neutral")) }
                .takeWhile { it.isLetter() }
        words.remove(type)

        return words
                .asSequence()
                .filterNot { it.isEmpty() }
                .toList() to type
    }

}

