package preprocessing

import logger.ParsingLogger
import java.io.File

object TextTokenHandler {

    private const val DEFAULT_FOLDER_PATH = "film-reviews/tokens"

    fun saveReviewsTokens(filmIdentifier: String, reviewsTokens: Iterable<Pair<Iterable<String>, String>>, index: Int) {
        ParsingLogger.log("Saving $filmIdentifier reviews tokens...")

        val folder = File(DEFAULT_FOLDER_PATH)
        folder.mkdirs()

        reviewsTokens.forEachIndexed { reviewIndex, review ->
            val file = File(folder, "$index - ${reviewIndex + 1} - $filmIdentifier.txt")
            file.createNewFile()

            file.printWriter().use {
                it.println(review.second + System.lineSeparator() + review.first.toString() + System.lineSeparator())
            }
        }
    }

}