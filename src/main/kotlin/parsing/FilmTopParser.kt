package parsing

import org.jsoup.Connection
import org.jsoup.Jsoup
import java.io.File
import java.util.*

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
    private const val userAGent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36"

    private var htmlFolder: File = File("html")

    init {
        if (!htmlFolder.exists()) {
            htmlFolder.mkdir()
        }
    }

    private fun getFilmReviewsURL(filmIdentifier: String, reviewCount: Int = DEFAULT_REVIEW_COUNT)
            = "https://www.kinopoisk.ru/film/$filmIdentifier/ord/rating/perpage/$reviewCount/#list"

    fun getTopFilmsIdentifiers(count: Int = DEFAULT_FILM_COUNT): List<String> {
        val connection = Jsoup.connect(FILM_TOP_URL)
                .userAgent(userAGent)
                .referrer(REFERRER)
                .cookies(cookies)

        val top250 = File(htmlFolder, "top250.html")
        if (!top250.exists()) {
            top250.printWriter().use {
                it.print(connection.get().toString())
            }
        }

        val doc = if (top250.exists()) {
            Jsoup.parse(top250.readText())
        } else {
            connection.get()
        }

        val regex = Regex("/film/(.+)/(.+)/")

        return (1..count)
                .map { doc.select("tr#top250_place_$it a.all[href]").toString() }
                .map { regex.find(it)!!.groups[1]!!.value }
                .toList()
    }

    fun getFilmReviews(filmIdentifier: String, reviewCount: Int = DEFAULT_REVIEW_COUNT): List<String> {
        val connection = Jsoup.connect(getFilmReviewsURL(filmIdentifier, reviewCount))
                .userAgent(userAGent)
                .referrer(REFERRER)
                .cookies(cookies)

        while (connection.get().baseUri().contains("showcaptcha")) {
            println("[${Date()}] Waiting...")
            Thread.sleep(900000)
        }

        val filmPage = File(htmlFolder, "$filmIdentifier.html")
        if (!filmPage.exists()) {
            File(htmlFolder, "$filmIdentifier.html").printWriter().use {
                it.print(connection.get().toString())
            }
        }

        val doc = if (filmPage.exists()) {
            Jsoup.parse(filmPage.readText())
        } else {
            connection.get()
        }

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