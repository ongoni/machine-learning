package parsing

import logger.ParsingLogger
import java.io.File

object FilmReviewHandler {

    private const val DEFAULT_FOLDER_PATH = "film-reviews"

    fun saveReviews(filmIdentifier: String, filmReviews: Iterable<String>, index: Int) {
        ParsingLogger.log("Saving $filmIdentifier reviews...")

        val folder = File(DEFAULT_FOLDER_PATH)
        folder.mkdir()
        val file = File(folder, "$index - $filmIdentifier.txt")
        file.createNewFile()

        file.printWriter().use {
            filmReviews.forEach { review ->
                it.println(review + System.lineSeparator())
            }
        }
    }

}