package parsing

import logger.ParsingLogger
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.io.File

object FilmTopParser {

    private const val KINOPOISK_URL = "https://www.kinopoisk.ru/"
    private const val FILM_TOP_URL = "https://www.kinopoisk.ru/top/"
    private const val REFERRER = "http://www.google.com"
    private const val DEFAULT_FILM_COUNT = 250
    private const val DEFAULT_REVIEW_COUNT = 50

    private var cookies = Jsoup.connect(KINOPOISK_URL)
            .method(Connection.Method.GET)
            .referrer(REFERRER)
            .execute()
            .cookies()
    private const val userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36"

    private var htmlFolder: File = File("html")

    init {
        if (!htmlFolder.exists()) {
            htmlFolder.mkdir()
        }
    }

    private fun getFilmReviewsURL(filmIdentifier: String, reviewCount: Int = DEFAULT_REVIEW_COUNT)
            = "https://www.kinopoisk.ru/film/$filmIdentifier/ord/rating/perpage/$reviewCount/#list"

    fun getTopFilmsIdentifiers(count: Int = DEFAULT_FILM_COUNT): List<String> {
        ParsingLogger.log("Top 250 parsing...")

        val connection = Jsoup.connect(FILM_TOP_URL)
                .userAgent(userAgent)
                .referrer(REFERRER)
                .cookies(cookies)

        val top250 = File(htmlFolder, "top250.html")
        if (!top250.exists()) {
            top250.printWriter().use {
                var document = connection.get()

                while (document.baseUri().contains("showcaptcha")) {
                    ParsingLogger.log("Waiting...")
                    Thread.sleep(900000)
                    document = connection.get()
                }

                it.print(connection.get().toString())
            }
        }

        val doc = Jsoup.parse(top250.readText())

        val regex = Regex("/film/(.+)/(.+)/")

        return (1..count)
                .map { doc.select("tr#top250_place_$it a.all[href]").toString() }
                .map { regex.find(it)!!.groups[1]!!.value }
    }

    fun getFilmReviews(filmIdentifier: String, reviewCount: Int = DEFAULT_REVIEW_COUNT): List<String> {
        ParsingLogger.log("$filmIdentifier reviews parsing...")

        val connection = Jsoup.connect(getFilmReviewsURL(filmIdentifier, reviewCount))
                .userAgent(userAgent)
                .referrer(REFERRER)
                .cookies(cookies)

        val filmPage = File(htmlFolder, "$filmIdentifier.html")
        if (!filmPage.exists()) {
            filmPage.printWriter().use {
                var document = connection.get()

                while (document.baseUri().contains("showcaptcha")) {
                    ParsingLogger.log("Waiting...")
                    Thread.sleep(900000)
                    document = connection.get()
                }

                it.print(connection.get().toString())
            }
        }

        val doc = Jsoup.parse(filmPage.readText())

        val result = mutableListOf<String>()
        doc.select(".clear_all .reviewItem .response").forEach {
            val reviewType = when(it.attr("class")) {
                "response good" -> "good"
                "response bad" -> "bad"
                else -> "neutral"
            }
            val reviewText = it.select(".brand_words ._reachbanner_").text()

            result.add(reviewType + System.getProperty("line.separator") + reviewText)
        }

        return result
    }

}