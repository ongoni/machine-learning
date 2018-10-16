package parsing

import java.io.File

object DataLoader {

    private const val DEFAULT_FOLDER_PATH = "film-reviews"

    fun getAndSaveReviews(filmIdentifiers: Iterable<String>) {
        filmIdentifiers.forEach {
            val folder = File(DEFAULT_FOLDER_PATH)
            folder.mkdir()
            val file = File(folder, "$it.txt")
            file.createNewFile()

            file.printWriter().use { writer ->
                FilmTopParser.getFilmReviews(it).forEach { review ->
                    writer.println(review)
                    writer.print(System.getProperty("line.separator"))
                    writer.print(System.getProperty("line.separator"))
                }
            }

            Thread.sleep((2..5).random() * 60000L)
        }
    }

}