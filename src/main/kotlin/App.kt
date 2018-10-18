import parsing.FilmReviewHandler
import parsing.FilmTopParser
import preprocessing.TextTokenHandler
import preprocessing.TextTokenizer
import utils.random

class App {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val filmIdentifiers = FilmTopParser.getTopFilmsIdentifiers()

            filmIdentifiers.forEachIndexed { index, identifier ->
                val reviews = FilmTopParser.getFilmReviews(identifier)
                val reviewsTokens = reviews.map { TextTokenizer.getTextTokens(it) }

                FilmReviewHandler.saveReviews(identifier, reviews, index + 1)
                TextTokenHandler.saveReviewsTokens(identifier, reviewsTokens, index + 1)

                Thread.sleep((2..5).random() * 5000L)
            }
        }
    }

}