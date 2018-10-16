import parsing.DataLoader
import parsing.FilmTopParser

class App {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val filmIdentifiers = FilmTopParser.getTopFilmsIdentifiers()

            DataLoader.getAndSaveReviews(filmIdentifiers)
        }
    }

}